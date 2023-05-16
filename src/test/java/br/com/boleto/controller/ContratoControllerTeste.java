package br.com.boleto.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ContratoControllerTeste {
//    private static final String TERMO = "/termo";
//
//	private static final String PRECO = "/preco";
//
//	private final String URL = "/contrato";
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//    
//    @Autowired
//    private ParceiroRepository parceiroRepository;
//    
//    @Autowired
//    private ServicoRepository servicoRepository;
//    
//    @Autowired
//    private ContaRepository contaRepository;
//    
//    @Autowired
//    private TermoRepository termoRepository;
//    
//    @Autowired
//    private ContratoRepository contratoRepository;
///*
//    @Test
//    public void getPrecoTeste(){
//    	
//    	Servico servico = servicoRepository.save(ModeloUtil.criaServico());
//    	Parceiro parceiro = parceiroRepository.save(ModeloUtil.criaParceiro());
//    	Termo termo = termoRepository.save(ModeloUtil.criarTermo());
//    	Conta conta = contaRepository.save(ModeloUtil.criaConta());
//    	contratoRepository.save(ModeloUtil.criaContrato(parceiro, servico, termo, conta));
//
//        HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<String> response = testRestTemplate
//        		.exchange(URL+PRECO+"?idapibanco=1&idparceiro=647&codserv=1&usuContratante=Sankhya Teste&versaoAceite=1", HttpMethod.GET, httpEntity, String.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//        
//        response = testRestTemplate
//        		.exchange(URL+PRECO+"?idapibanco=&idparceiro=647&codserv=1&usuContratante=Sankhya Teste&versaoAceite=1", HttpMethod.GET, httpEntity, String.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
//    }
//    
//    @Test
//    public void getTermoTest() {
//    	Parceiro parceiro = parceiroRepository.save(ModeloUtil.criaParceiro());
//    	Servico servico = servicoRepository.save(ModeloUtil.criaServico());
//    	Conta conta = contaRepository.save(ModeloUtil.criaConta());
//	   
//    	HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<TermoDto> response = testRestTemplate
//                .exchange(URL+TERMO+"/?idparceiro="+parceiro.getId()+"&codserv="+servico.getCodserv()+"&idapibanco="+conta.getId(), HttpMethod.GET, httpEntity, TermoDto.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//        
//        response = testRestTemplate.exchange(URL+TERMO+"/?idparceiro="+parceiro.getId()+"&codserv=", HttpMethod.GET, httpEntity, TermoDto.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
//    }
//*/
//    @Test
//    public void salvaContratoTeste() {
//    	
//    	Parceiro parceiro = parceiroRepository.save(ModeloUtil.criaParceiro());
//    	Servico servico = servicoRepository.save(ModeloUtil.criaServico());
//    	Conta conta = contaRepository.save(ModeloUtil.criaConta());
//    	Termo termo = termoRepository.save(ModeloUtil.criarTermo());
//    	contratoRepository.save(ModeloUtil.criaContrato(parceiro, servico, termo, conta));
//    	
//    	ContratoDto contratoDto = ModeloUtil.criaContratoDto();
//    	
//    	HttpEntity httpEntity = new HttpEntity<>(contratoDto, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ResponseDto> response = testRestTemplate.exchange(URL, HttpMethod.POST, httpEntity, ResponseDto.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//        
//        contratoDto.setIdapibanco(0);
//        
//        response = testRestTemplate.exchange(URL, HttpMethod.POST, httpEntity, ResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
//    	
//    }
   
}