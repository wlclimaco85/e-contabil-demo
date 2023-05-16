package br.com.boleto.service.implementation;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.boleto.enums.JobEnum;
import br.com.boleto.persistence.dtos.AcaoRetornoDto4;
import br.com.boleto.persistence.dtos.AcaoRetornoDto5;
import br.com.boleto.persistence.dtos.LoginResponseDto;
import br.com.boleto.persistence.entity.Acoes;
import br.com.boleto.persistence.entity.Job;
import br.com.boleto.persistence.entity.Ordens;
import br.com.boleto.persistence.repository.AcoesRepository;
import br.com.boleto.persistence.repository.JobRepository;
import br.com.boleto.persistence.repository.OrdensRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoletoService {
    
    @Autowired
    private BreakevenService breakevenService;

   
    @Autowired
    private AcoesRepository acoesRepository;


    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private OrdensRepository ordensRepository;



    private HttpHeaders createJSONHeader(LoginResponseDto loginResponseDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + loginResponseDto.getAccess_token());
        headers.add("Content-Type", "application/json");
        return headers;
    }
    
    private HttpHeaders createJSONHeaderAcao() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");

        return headers;
    }
    
    private HttpHeaders createJSONHeaderAcao2() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("accept", "*/*");
        headers.add("accept-language", "pt");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");

        return headers;
    }
    
    private HttpHeaders createJSONHeaderAcao3() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return headers;
    }

    
    private String getDefaultQueryParameters(String query) {
        return "https://brapi.dev/api/quote/" + query;
    }

    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
    
    public void atualizaBoletosBaixados(Job job) {

		Integer count = 0;
		String acao = "";
		try {
			count += AtualizaAcoes();
			count += AtualizaOrdens();
		} catch (Exception e) {
			job.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if (count > 0 || job.getError() != null) {
				job.setQtdRegistro(count);
				job.setDataFinal(Timestamp.valueOf(LocalDateTime.now()));
				Timestamp proxEx = Timestamp
						.valueOf(LocalDateTime.now().plusMinutes(JobEnum.valueByTempo(job.getTipo())));
				job.setDataProxExec(proxEx);
				job.setStatus(1);
				jobRepository.save(job);
			} else {
				jobRepository.deleteById(job.getId());
			}
		}

	}

	private Integer AtualizaAcoes() {
		Integer count = 0;
		String acao = "";
		List<String> acoes = new ArrayList<>();
		acoes = acoesRepository.findDistinctByAcoes();
		for (String acoes2 : acoes) {
			acao = acao + acoes2.substring(0, acoes2.length() - 1) + ",";
		}
		if (acao != null && acao.length() > 1) {
			acao = acao.substring(0, acao.length() - 1);

			HttpEntity<?> httpEntity = new HttpEntity<>(createJSONHeaderAcao());

			String params = "" + acao;

			try {
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(
						getDefaultQueryParameters(params), HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);

				for (AcaoRetornoDto4 string : response.getBody().getResults()) {
					if (string.getRegularMarketPrice() != null) {
						acoesRepository.alteraPriceCurrency(string.getRegularMarketPrice(), LocalDateTime.now(),
								string.getShortName(), string.getSymbol());
						List<Acoes> acoess = acoesRepository.findDistinctByAcoes(string.getSymbol());
						for (Acoes acoesss : acoess) {
							if ("C".equals(acoesss.getTipo())) {
								acoesss.setLucropreju(string.getRegularMarketPrice() - (acoesss.getValorsuj() != null ? acoesss.getValorsuj() : 0) );
							} else {
								acoesss.setLucropreju((acoesss.getValorsuj() != null ? acoesss.getValorsuj() : 0) - string.getRegularMarketPrice());
							}
							acoesRepository.save(acoesss);
						}
					}

				}

				System.out.println("teste");
			} catch (HttpClientErrorException ee) {
				httpEntity = new HttpEntity<>(createJSONHeaderAcao2());
				params = "" + acao;
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(
						getDefaultQueryParameters(params), HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);

				System.out.println("teste");
			} catch (IllegalArgumentException eed) {
				httpEntity = new HttpEntity<>(createJSONHeaderAcao3());
				params = "" + acao;
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(
						getDefaultQueryParameters(params), HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);

				System.out.println("teste");
			}
		}
		return count;
	} 
	
	private Integer AtualizaOrdens() {
		Integer count = 0;
		String acao = "";
		List<String> acoes = new ArrayList<>();
		acoes = ordensRepository.findDistinctByAcoes();
		for (String acoes2 : acoes) {
			acao = acao + acoes2.substring(0, acoes2.length() - 1) + ",";
		}
		if (acao != null && acao.length() > 1) {
			acao = acao.substring(0, acao.length() - 1);

			HttpEntity<?> httpEntity = new HttpEntity<>(createJSONHeaderAcao());

			String params = "" + acao;

			try {
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(
						getDefaultQueryParameters(params), HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);

				for (AcaoRetornoDto4 string : response.getBody().getResults()) {
					if (string.getRegularMarketPrice() != null) {
						ordensRepository.alteraPriceCurrency(string.getRegularMarketPrice(), LocalDateTime.now(), string.getSymbol());
						List<Ordens> acoess = ordensRepository.findDistinctByAcoes(string.getSymbol());
						for (Ordens acoesss : acoess) {
							if ("C".equals(acoesss.getTipo())) {
								acoesss.setLucropreju(string.getRegularMarketPrice() - (acoesss.getValor() != null ? acoesss.getValor() : 0) );
							} else {
								acoesss.setLucropreju((acoesss.getValor() != null ? acoesss.getValor() : 0) - string.getRegularMarketPrice());
							}
							ordensRepository.save(acoesss);
						}
					}
				}

				System.out.println("teste");
			} catch (HttpClientErrorException ee) {
				httpEntity = new HttpEntity<>(createJSONHeaderAcao2());
				params = "" + acao;
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(
						getDefaultQueryParameters(params), HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);

				System.out.println("teste");
			} catch (IllegalArgumentException eed) {
				httpEntity = new HttpEntity<>(createJSONHeaderAcao3());
				params = "" + acao;
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(
						getDefaultQueryParameters(params), HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);

				System.out.println("teste");
			}
		}
		return count;
	}  
 	
	public void atualizarStopLosseGain(Job job) {

		Integer count = 0;
		String acao = "";
		try {
			List<Acoes> acoes = new ArrayList<Acoes>();
			Map<String,Acoes> example = new HashMap<String,Acoes>();
			acoes = acoesRepository.findByStatus("D");
			for (Acoes acoes3 : acoes) {
				String acoes2 = acoes3.getAcao(); 
				example.put( acoes2.substring(0, acoes2.length() - 1), acoes3);
				acao = acao + acoes2.substring(0, acoes2.length() - 1) + ",";
			}
			if (acao != null && acao.length() > 1) {
				acao = acao.substring(0, acao.length() - 1);

				HttpEntity<?> httpEntity = new HttpEntity<>(createJSONHeaderAcao());

				String params = "" + acao;

				try {
					ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(
							getDefaultQueryParameters(params), HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);
					Double pAcao = 0.0;
					for (AcaoRetornoDto4 string : response.getBody().getResults()) {
						//TODO
//						if (string.getRegularMarketPrice() != null) {
//							Acoes acao1 = example.get(string.getSymbol());
//							Double novoLoss = acao1.getLoss() != null ? acao1.getLoss() : 0.0 ;
//							Double novoGain = acao1.getGain() != null ? acao1.getGain() : 0.0 ;
//							
//							if(acao1.getLossCorrente() != null) {
//								novoLoss = acao1.getLossCorrente();
//							}
//							if(acao1.getGainCorrente() != null) {
//								novoGain = acao1.getGainCorrente();
//							}
//								if ("C".equals(acao1.getTipo())) {
//									pAcao = ((acao1.getValoracaoatual() - (acao1.getValor() == null ? acao1.getValorsuj() : acao1.getValor())) / 0.01);
//								} else {
//									pAcao = (((acao1.getValor() == null ? acao1.getValorsuj() : acao1.getValor()) - acao1.getValoracaoatual()) / 0.01);
//								}
//								if(pAcao >= 1) {
//									Breakeven breakeven = new Breakeven();
//									breakeven.setAcao(acao1.getAcao());
//									breakeven.setAcaoId(acao1.getId());
//									breakeven.setGainAtual(novoGain*pAcao);
//									breakeven.setLossAtual(novoLoss*pAcao);
//									breakeven.setValorAtualAcao(string.getRegularMarketPrice());
//									breakeven.setDh_created_at(LocalDateTime.now());
//									breakeven.setStatus("A");
//									breakevenService.insert(breakeven);
//									acao1.setValor(string.getRegularMarketPrice());
//									acao1.setValoracaoatual(string.getRegularMarketPrice());
//									acao1.setLossCorrente(novoLoss*pAcao);
//									acao1.setGainCorrente(novoGain*pAcao);
//									acoesRepository.save(acao1);
//								}
//							
//						}

					}
				} catch (HttpClientErrorException ee) {
					httpEntity = new HttpEntity<>(createJSONHeaderAcao2());
					params = "" + acao;
					ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(
							getDefaultQueryParameters(params), HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);
				} catch (IllegalArgumentException eed) {
					httpEntity = new HttpEntity<>(createJSONHeaderAcao3());
					params = "" + acao;
					ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(
							getDefaultQueryParameters(params), HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);
				}
			}
		} catch (Exception e) {
			job.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if (count > 0 || job.getError() != null) {
				job.setQtdRegistro(count);
				job.setDataFinal(Timestamp.valueOf(LocalDateTime.now()));
				Timestamp proxEx = Timestamp
						.valueOf(LocalDateTime.now().plusMinutes(JobEnum.valueByTempo(job.getTipo())));
				job.setDataProxExec(proxEx);
				job.setStatus(1);
				jobRepository.save(job);
			} else {
				jobRepository.deleteById(job.getId());
			}
		}

	}  
  
}