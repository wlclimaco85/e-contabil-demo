package br.com.boleto.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Optional;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import br.com.boleto.exception.FailedAuthenticationException;
import br.com.boleto.persistence.entity.Certificado;
import br.com.boleto.persistence.repository.CertificadoRepository;



@Configuration
public class RestTemplateConf {
	@Value("${pass.file.p12}")
    private String passFile;
	
	@Autowired
	private CertificadoRepository certificadoRepository;

    public RestTemplate preparaP12(Integer conta_id) {
        try {      	
        	Optional<Certificado> certificadoOpt = certificadoRepository.buscaCertificadoConta(conta_id);
            InputStream input = new ByteArrayInputStream(certificadoOpt.get().getP12());
            KeyStore clientStore = KeyStore.getInstance("PKCS12");

            clientStore.load(input, passFile.toCharArray());

            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.setProtocol("TLS");
            sslContextBuilder.loadKeyMaterial(clientStore, passFile.toCharArray());
            sslContextBuilder.loadTrustMaterial(new TrustSelfSignedStrategy());

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setConnectTimeout(10000); 
            requestFactory.setReadTimeout(10000); 
            return new RestTemplate(requestFactory);
        }catch (Exception e){
            throw new FailedAuthenticationException("Não foi possivel validar o certificado p12.");
        }
    }
}
