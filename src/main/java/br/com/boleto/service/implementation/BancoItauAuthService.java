package br.com.boleto.service.implementation;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.exception.FailedAuthenticationException;
import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.LoginResponseDto;
import br.com.boleto.persistence.entity.Certificado;
import br.com.boleto.persistence.repository.CertificadoRepository;
import br.com.boleto.persistence.repository.ContaRepository;
import br.com.boleto.service.AuthenticationService;
import br.com.boleto.util.RestTemplateConf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BancoItauAuthService implements AuthenticationService {
	@Value("${url.api.itau.envio.csr}")
	private String URLENVIOCSR;
	
	@Value("${path.api.itau.pem}")
    private String localChavesPEM;

	@Value("${folder.file.csr}")
	private String FILECSR;

	@Value("${folder.file.key}")
	private String FILEKEY;

	@Value("${folder.file.crt}")
	private String FILECRT;

	@Value("${folder.file.p12}")
	private String FILEP12;

	@Value("${url.api.itau.auth}")
	private String URLAUTH;

	@Autowired
	private CertificadoRepository certificadoRepository;

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplateConf restTemplate;

	@Autowired
	private ContaRepository contaRepository;

	@Override
	public BancoEnum getType() {
		return BancoEnum.ITAU;
	}

	@Override
	public LoginResponseDto authentication(LoginDto loginDto) {

		try {
			RestTemplate rest = new RestTemplate();

			if(environment.getActiveProfiles().equals("prd")){
				rest = restTemplate.preparaP12(loginDto.getConta_id());
			}
			rest = restTemplate.preparaP12(loginDto.getConta_id());

			List<MediaType> acceptableMediaTypes = new ArrayList<>();

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/x-www-form-urlencoded");

			MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
			requestBody.add("grant_type", "client_credentials");
			requestBody.add("client_id", loginDto.getClient_id());
			requestBody.add("client_secret", loginDto.getClient_secret());

			acceptableMediaTypes.add(MediaType.ALL);
			headers.setAccept(acceptableMediaTypes);

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
			ResponseEntity<LoginResponseDto> response = rest.exchange(URLAUTH, HttpMethod.POST, entity,
					LoginResponseDto.class);
			return response.getBody();

		}  catch (RuntimeException exception){
			log.error("As credenciais inseridas apresentaram erro no teste de integração. [Método: authentication]");
			log.error(exception.getMessage(), exception);
			throw new FailedAuthenticationException("O teste das credenciais com o banco Itau falhou. Por favor, tente novamente.");
		}
	}

	@Override
	public Certificado certificado(LoginDto loginDto) {
		Optional<Certificado> certificadoOpt = certificadoRepository.buscaCertificadoConta(loginDto.getConta_id());
		String nomeArquivo = loginDto.getConta_id().toString();

		if (certificadoOpt.isPresent()) {
			Certificado certificadoAtual = certificadoOpt.get();
			LoginDto loginDtoAuth = new LoginDto(certificadoOpt.get().getClientid(),
					certificadoOpt.get().getClientsecret(), null, certificadoAtual.getConta().getId(), null, null);
			authentication(loginDtoAuth);
			return certificadoAtual;
		}

		return geraCertificado(loginDto, nomeArquivo);
	}

	@Transactional
	private Certificado geraCertificado(LoginDto loginDto, String nomeArquivo) {
		byte[] chaveSessaoDecifrada = decriptografaRsa(loginDto.getChave_sessao());
		SecretKey chaveSessao = new SecretKeySpec(chaveSessaoDecifrada, 0, chaveSessaoDecifrada.length, "AES");
		String clientIdDecifrada = decriptografaAes(chaveSessao, loginDto.getClient_id());
		String tokenDecifrado = decriptografaAes(chaveSessao, loginDto.getToken());

		if (clientIdDecifrada.isEmpty() || tokenDecifrado.isEmpty()) {
			throw new FailedAuthenticationException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
		}

		geraArquivoCsreKey(nomeArquivo, clientIdDecifrada);

		String clientSecret = envioCSR(tokenDecifrado, nomeArquivo);
		geraP12(nomeArquivo);

		byte[] fileCsr = converteArquivoBytes(FILECSR, nomeArquivo, ".csr");
		byte[] fileKey = converteArquivoBytes(FILEKEY, nomeArquivo, ".key");
		byte[] fileCRT = converteArquivoBytes(FILECRT, nomeArquivo, ".crt");
		byte[] p12 = converteArquivoBytes(FILEP12, nomeArquivo, ".p12");

		Certificado certificado = new Certificado();
		certificado.setConta(contaRepository.findById(loginDto.getConta_id()).get());
		certificado.setClientid(clientIdDecifrada);
		certificado.setClientsecret(clientSecret);
		certificado.setToken(tokenDecifrado);
		certificado.setCrt(fileCRT);
		certificado.setCsr(fileCsr);
		certificado.setKey(fileKey);
		certificado.setP12(p12);

		return certificadoRepository.save(certificado);
	}

	public static byte[] converteArquivoBytes(String path, String nomeArquivo, String ext) {
		try {
			File file = new File(path.concat(nomeArquivo).concat(ext));
			byte[] bytes = new byte[(int) file.length()];
			try (FileInputStream fis = new FileInputStream(file)) {
				fis.read(bytes);
			}
			return bytes;
		} catch (Exception e) {
			log.error("Erro ao converter arquivo "+nomeArquivo.concat(".").concat(ext)+"em bytes. [Método: converteArquivoBytes]");
			log.error(e.getMessage(), e);
			throw new NotFoundException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
		}
	}

	private String extraiChaveRsaPem(String tipoChave) {
		try {
			File file = ResourceUtils.getFile(localChavesPEM +"/private.pem");
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			boolean inKey = false;
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (!inKey) {
					if (line.startsWith("-----BEGIN ") && line.endsWith(" " + tipoChave + " KEY-----")) {
						inKey = true;
					}
				} else {
					if (line.startsWith("-----END ") && line.endsWith(" " + tipoChave + " KEY-----")) {
						inKey = false;
						break;
					}
					sb.append(line);
				}
			}
			return sb.toString();
		} catch (Exception e) {
			log.error("Erro ao extrair chaves PEM.[Método: extraiChaveRsaPem]");
			log.error(e.getMessage(), e);
			throw new NotFoundException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
		}
	}

	public static String decriptografaAes(SecretKey key, String cipherText) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(new byte[16]);
			cipher.init(Cipher.DECRYPT_MODE, key, iv);
			byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

			return new String(plainText);
		} catch (Exception e) {
			log.error("Erro ao descriptografar chaves.[Método: decriptografaAes]");
			log.error(e.getMessage(), e);
			throw new NotFoundException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
		}
	}

	private byte[] decriptografaRsa(String dadosCifrados) {
		try {
			String chavePrivada = extraiChaveRsaPem("PRIVATE");
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(chavePrivada));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(spec);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(Base64.getDecoder().decode(dadosCifrados));
		} catch (Exception e) {
			log.error("Erro ao descriptografar as chaves.[Método: decriptografaRsa]");
			log.error(e.getMessage(), e);
			throw new NotFoundException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
		}
	}

	private void geraArquivoCsreKey(String nomeArquivo, String clientid) {
		String arquivoCSR = FILECSR.concat(nomeArquivo).concat(".csr");
		String arquivoKEY = FILEKEY.concat(nomeArquivo).concat(".key");
		String endereco = "/CN=".concat(clientid).concat("/OU=sankhya.com.br/L=UBERLANDIA/ST=MG/C=BR");
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		String comandBySO = (isWindows) ? "C:/Program Files/Git/usr/bin/openssl.exe" : "openssl";
		ProcessBuilder processbuilder = new ProcessBuilder(comandBySO, "req", "-new", "-subj", endereco, "-out",
				arquivoCSR, "-nodes", "-sha512", "-newkey", "rsa:2048", "-keyout", arquivoKEY);

		processbuilder.redirectErrorStream(true);
		Process process = null;
		try {
			process = processbuilder.start();
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			StringBuilder s = new StringBuilder();
			while ((line = input.readLine()) != null) {
				s.append(line);
			}

			int status = process.waitFor();
			if (status != 0) {
				String message = s.toString();
				throw new Exception(message);
			}

		} catch (Exception e) {
			log.error("Erro ao gerar arquivo csr e key. [Método: gerarArquivoCsreKey]");
			log.error(e.getMessage(), e);
			throw new NotFoundException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}

	public String envioCSR(String token, String nomeArquivo){
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);
			headers.add("Content-Type", "text/plain");

			List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
			InputStream inputStream = new FileInputStream(new File(FILECSR.concat(nomeArquivo).concat(".csr")));
			InputStream is = new BufferedInputStream(inputStream);
			byte[] bytes = is.readAllBytes();

			acceptableMediaTypes.add(MediaType.ALL);
			headers.setAccept(acceptableMediaTypes);
			HttpEntity<byte[]> entity = new HttpEntity<>(bytes, headers);
			ResponseEntity<String> registroResponse = new RestTemplate().exchange(URLENVIOCSR, HttpMethod.POST, entity,
					String.class);

			return extraiClientSecret(registroResponse.getBody().toString(), nomeArquivo);

		} catch (Exception e) {
			log.error("Erro ao gerar o arquivo CRT.[Método: envioCSR]");
			log.error(e.getMessage(), e);
			throw new NotFoundException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
		}
	}

	private String extraiClientSecret(String response, String nomeArquivo) {
		String pathFileCRT = FILECRT.concat(nomeArquivo).concat(".crt");
		try {
			InputStream is = new ByteArrayInputStream(response.getBytes());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder clientSecret = new StringBuilder();
			StringBuilder crtBody = new StringBuilder();
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				if (line.startsWith("Secret: ")) {
					clientSecret.append(line.replace("Secret: ", "").trim());
				} else {
					crtBody.append(line).append("\n");
				}
			}

			File file = new File(pathFileCRT);
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fileWriter = new FileWriter(file);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(crtBody);
			printWriter.close();

			return clientSecret.toString();
		} catch (Exception e) {
			log.error("Erro ao extrair o cliente secret.[Método: extraiClientSecret]");
			log.error(e.getMessage(), e);
			throw new NotFoundException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
		}
	}

	public void geraP12(String nomeArquivo) {
		String arquivoCRT = FILECRT.concat(nomeArquivo).concat(".crt");
		String arquivoKEY = FILEKEY.concat(nomeArquivo).concat(".key");
		String p12 = FILEP12.concat(nomeArquivo).concat(".p12");
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		String homeDirectory = (isWindows) ? "C:/Program Files/Git/usr/bin/openssl.exe" : "openssl";
		ProcessBuilder processbuilder = new ProcessBuilder(homeDirectory, "pkcs12", "-export", "-inkey", arquivoKEY,
				"-in", arquivoCRT, "-name", "sankhya", "-out", p12, "-password", "pass:20a75073");

		processbuilder.redirectErrorStream(true);
		Process process = null;
		try {
			process = processbuilder.start();
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			StringBuilder s = new StringBuilder();
			while ((line = input.readLine()) != null) {
				s.append(line);
			}

			int status = process.waitFor();
			if (status != 0) {
				String message = s.toString();
				throw new Exception(message);
			}

		} catch (Exception e) {
			log.error("Erro ao gerar o arquivo P12.[Método: geraP12]");
			log.error(e.getMessage(), e);
			throw new NotFoundException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}
}
