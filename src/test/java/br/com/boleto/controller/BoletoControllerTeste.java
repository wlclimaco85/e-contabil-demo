package br.com.boleto.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BoletoControllerTeste {
//	
//	private static final String GET_BOLETO_BANCO = "/getBoletoBanco";
//
//	private static final String ATUALIZAR_STATUS_BOLETO_POR_CONTA = "/atualizarStatusBoletoPorConta";
//
//	private static final String PAGE_1 = "?page=1";
//
//	private static final String GETBOLETOS_SEARCH = "/getboletosSearch";
//
//	private static final String GETBOLETOS = "/getboletos";
//
//	private final String REGISTRA_BOLETO = "/registraBoleto";
//
//	private final String FILTRA_BOLETOS = "/filtraBoletos";
//
//	private final String FILTRA_BOLETO = "/filtraBoleto";
//
//	private final String CANCELAR = "/cancelar";
//
//	private final String ASSOCIAR_NUFIN = "/associarNufin";
//
//	private final String GET_EMVPIX_BOLETO = "/getEmvpixBoleto";
//
//	private final String GET_BOLETO_SEM_NUFIN = "/getBoletoSemNufin";
//
//	private final String GET_STATUS = "/getStatus";
//
//	private final String URL = "/boleto";
//	
//	@Autowired
//    private TestRestTemplate testRestTemplate;
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
//	@Test
//	public void salvaAlteraBoletoTeste() {
//		
//		parceiroRepository.save(ModeloUtil.criaParceiro());
//		Conta conta = contaRepository.save(ModeloUtil.criaConta());
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		ArrayList<Map<String, Object>> itens = BoletoUtil.prencheDadosRequestBodyBoletoDto(conta.getId());
//		
//		HttpEntity httpEntity = new HttpEntity<>(itens, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//
//	@Test
//	public void getStatusTeste() {
//		
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		List<StatusRequestDto> statusRequestDto = BoletoUtil.criaStatusRequestDto();
//		
//		HttpEntity httpEntity = new HttpEntity<>(statusRequestDto, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL+GET_STATUS, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//        
//	}
//	
//	@Test
//	public void getEmvpixBoletoTeste() {
//		
//		Conta conta = contaRepository.save(ModeloUtil.criaConta());
//		Boleto boleto = boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		ArrayList<Map<String, Object>> itens = BoletoUtil.prencheDadosRequestBodyBoletoDto(conta.getId());
//		
//		HttpEntity httpEntity = new HttpEntity<>(itens, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL+GET_EMVPIX_BOLETO, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void getBoletoSemNufinTeste() {
//		
//		HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL+GET_BOLETO_SEM_NUFIN, HttpMethod.GET, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void associarNufinTeste() {
//		Conta conta = contaRepository.save(ModeloUtil.criaConta());
//		Boleto boleto = boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		ArrayList<Map<String, Object>> itens = BoletoUtil.prencheDadosRequestBodyBoletoDto(conta.getId());
//		
//		List<BoletoDto> boletoDto = BoletoUtil.criaBoletoDto();
//		
//		HttpEntity httpEntity = new HttpEntity<>(itens, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL+ASSOCIAR_NUFIN, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void pesquisarTeste() {
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<BoletoDto> response = testRestTemplate.exchange(URL +"/"+ 1, HttpMethod.GET, httpEntity, BoletoDto.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//        
//		response = testRestTemplate.exchange(URL +"/"+ 0, HttpMethod.GET, httpEntity, BoletoDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
//	}
//    
//	@Test
//	public void cancelaBoletoTeste() {
//		Conta conta = contaRepository.save(ModeloUtil.criaConta());
//		Boleto boleto = boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		ArrayList<Map<String, Object>> itens = BoletoUtil.prencheDadosRequestBodyBoletoDto(conta.getId());
//		
//		HttpEntity httpEntity = new HttpEntity<>(itens, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL + CANCELAR, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void filtraBoletoTeste() {
//		
//		parceiroRepository.save(ModeloUtil.criaParceiro());
//		contaRepository.save(ModeloUtil.criaConta());
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		Map<String, Object> map = BoletoUtil.criaStatusBoletoFilterRequest();
//		
//		HttpEntity httpEntity = new HttpEntity<>(map, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL + FILTRA_BOLETO, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void filtraBoletosTeste() {
//		
//		parceiroRepository.save(ModeloUtil.criaParceiro());
//		contaRepository.save(ModeloUtil.criaConta());
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		ArrayList<StatusBoletoFilterRequestDto> itens = BoletoUtil.criaStatusBoletoFilterRequestList();
//		
//		HttpEntity httpEntity = new HttpEntity<>(itens, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL + FILTRA_BOLETOS, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void registraBoletoTeste() {
//		
//		parceiroRepository.save(ModeloUtil.criaParceiro());
//		Conta conta = contaRepository.save(ModeloUtil.criaConta());
//		Boleto boleto = boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		ArrayList<Map<String, Object>> itens = BoletoUtil.prencheDadosRequestBodyBoletoDto(conta.getId());
//		
//		HttpEntity httpEntity = new HttpEntity<>(itens, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL + REGISTRA_BOLETO, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void buscaBoletosPaginadosFiltroTeste() {
//		
//		parceiroRepository.save(ModeloUtil.criaParceiro());
//		contaRepository.save(ModeloUtil.criaConta());
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		Map<String, Object> map = BoletoUtil.criaStatusBoletoFilterRequest();
//		
//		HttpEntity httpEntity = new HttpEntity<>(map, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<BoletoResponseDto> response = testRestTemplate.exchange(URL + GETBOLETOS + PAGE_1, HttpMethod.POST, httpEntity, BoletoResponseDto.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void buscaBoletosPaginadosFiltroSearchTeste() {
//		
//		parceiroRepository.save(ModeloUtil.criaParceiro());
//		contaRepository.save(ModeloUtil.criaConta());
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		StatusBoletoFilterSearchRequestDto statusBoletoFilterSearchRequestDto = new StatusBoletoFilterSearchRequestDto();
//		
//		HttpEntity httpEntity = new HttpEntity<>(statusBoletoFilterSearchRequestDto, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL + GETBOLETOS_SEARCH + PAGE_1, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void atualizarStatusBoletoPorContaTeste() {
//		
//		parceiroRepository.save(ModeloUtil.criaParceiro());
//		contaRepository.save(ModeloUtil.criaConta());
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		Map<String, Object> map = BoletoUtil.criaDadosAtualizacaoBoletosRequest();
//		
//		HttpEntity httpEntity = new HttpEntity<>(map, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<String> response = testRestTemplate.exchange(URL + ATUALIZAR_STATUS_BOLETO_POR_CONTA + PAGE_1, HttpMethod.POST, httpEntity, String.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
//	
//	@Test
//	public void getBoletoBancoTeste() {
//		
//		parceiroRepository.save(ModeloUtil.criaParceiro());
//		contaRepository.save(ModeloUtil.criaConta());
//		boletoRepository.save(BoletoUtil.criaBoleto());
//		
//		Map<String, Object> map = BoletoUtil.criaStatusBoletoFilterRequest();
//		
//		HttpEntity httpEntity = new HttpEntity<>(map, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<BoletoRetornoDto> response = testRestTemplate.exchange(URL+GET_BOLETO_BANCO+PAGE_1, HttpMethod.POST, httpEntity, BoletoRetornoDto.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//	}
}
