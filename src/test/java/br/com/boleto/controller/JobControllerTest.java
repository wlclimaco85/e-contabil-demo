package br.com.boleto.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class JobControllerTest {
//	
//	private static final String JOBS_LIST = "/jobsList";
//
//	private static final String JOBS = "/jobs";
//
//	private static final String TIPO = "/tipo";
//
//	private final String URL = "/job";
//	
//	private static final String PAGE_1 = "?page=1";
//	
//	@Autowired
//	private BoletoRepository boletoRepository;
//	
//	@Autowired
//	private ContaRepository contaRepository;
//	
//	@Autowired 
//	private ParceiroRepository parceiroRepository;
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//	
//	@Test
//    public void pesquisaBancoPorIdTeste(){
//
//        HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());
//        
//        ResponseEntity<JobResponseDto> response = testRestTemplate.exchange(URL +"/"+ 100, HttpMethod.GET, httpEntity, JobResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//
//    }
//
//	@Test
//    public void pesquisaBancoPorTipoTeste(){
//		HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());
//        
//        ResponseEntity<JobResponseDto> response = testRestTemplate.exchange(URL+TIPO +"/"+ 1, HttpMethod.GET, httpEntity, JobResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//    }
//    
//	@Test
//    public void buscaTodosBancosTest() {
//    	HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<JobResponseDto> response = testRestTemplate.exchange(URL, HttpMethod.GET, httpEntity, JobResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//    }
//    /*
//    @Test
//    public void buscaBoletosPaginadosFiltroSearchTeste(){
//    	
//    	parceiroRepository.save(ModeloUtil.criaParceiro());
//		contaRepository.save(ModeloUtil.criaConta());
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		StatusBoletoFilterSearchRequestDto statusBoletoFilterSearchRequestDto = BoletoUtil.criaStatusBoletoFilterSearchRequestDto();
//		
//		HttpEntity httpEntity = new HttpEntity<>(statusBoletoFilterSearchRequestDto, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ListaStatusResponseDto> response = testRestTemplate.exchange(URL+JOBS+PAGE_1, HttpMethod.POST, httpEntity, ListaStatusResponseDto.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//    	
//    }
//    
//    @Test
//    public void findByJobRequestTeste(){
//    	
//    	parceiroRepository.save(ModeloUtil.criaParceiro());
//		contaRepository.save(ModeloUtil.criaConta());
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		StatusBoletoFilterSearchRequestDto statusBoletoFilterSearchRequestDto = BoletoUtil.criaStatusBoletoFilterSearchRequestDto();
//		
//		HttpEntity httpEntity = new HttpEntity<>(statusBoletoFilterSearchRequestDto, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<Job> response = testRestTemplate.exchange(URL+JOBS_LIST+PAGE_1, HttpMethod.POST, httpEntity, Job.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//    	
//    }*/
//
//	public static void main(String[] args) {
//		Timestamp proxEx = Timestamp.valueOf(LocalDateTime.now().minusHours(168));
//		Timestamp proxEx2 = Timestamp.valueOf(LocalDateTime.now().minusHours(24));
//		System.out.println(LocalDateTime.now());
//		System.out.println(LocalDateTime.now().minusHours(168));
//		System.out.println(LocalDateTime.now().minusHours(24));
//	}
}
