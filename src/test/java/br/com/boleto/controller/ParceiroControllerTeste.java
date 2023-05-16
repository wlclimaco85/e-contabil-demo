package br.com.boleto.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ParceiroControllerTeste {

//	private static final String PARCEIROS = "/parceiros";
//
//	private final String URL = "/parceiro";
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//    
//    @Autowired
//    private ParceiroRepository parceiroRepository;
//	
//	@Test
//    public void pesquisaParceiroPorIdTeste(){
//
//		//parceiroRepository.save(ModeloUtil.criaParceiro());
//
//        HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ParceiroResponseDto> response = testRestTemplate.exchange(URL +"/72", HttpMethod.GET, httpEntity, ParceiroResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//
//        response = testRestTemplate.exchange(URL +"/0", HttpMethod.GET, httpEntity, ParceiroResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
//
//    }
//	
//	@Test
//	public void findByParceiroRequestTeste() {
//		
//		ParceiroSearchRequestDto ParceiroSearchRequestDto = ModeloUtil.criaParceiroSearchRequestDto();
//		
//		HttpEntity httpEntity = new HttpEntity<>(ParceiroSearchRequestDto, RequestUtilTeste.buscaHttpHeadersJson());
//
//        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL+PARCEIROS, HttpMethod.POST, httpEntity, ArrayList.class);
//        
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//        
//	}
//	
//	/*@GetMapping("/{id}")
//    public ResponseEntity<ParceiroResponseDto> pesquisaParceiroPorId(@PathVariable("id") Integer id) {
//        return new ResponseEntity<>(parceiroService.pesquisaParceiroPorStatus(id), HttpStatus.OK);
//    }
//    
//    @PostMapping("/parceiros")
//    public ResponseEntity<ArrayList<Parceiro>>  findByParceiroRequest(@RequestBody ParceiroSearchRequestDto filter, Pageable pageable){
//		return new ResponseEntity<>(parceiroService.findByParceiroRequest(filter,pageable), HttpStatus.OK);
//	}*/
}
