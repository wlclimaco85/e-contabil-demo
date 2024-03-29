package br.com.boleto.service;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContratoServiceTeste {

//    @InjectMocks
//    private ContratoService contratoService;
//
//    @Mock
//    private ContratoRepository contratoRepository;
//
//    @Mock
//    private ServicoRepository servicoRepository;
//
//    @Mock
//    private TermoRepository termoRepository;
//
//    @Mock
//    private ContratoMapper contratoMapper;
//
//    @Mock
//    private TermoMapper termoMapper;
//
//    private Contrato contrato;
//
//    private Servico servico;
//
//    private ContratoDto contratoDto;
//
//    private Termo termo;
//
//
//    @Before
//    public void setUp() {
//        contrato = criaContrato(criaParceiro(), criaServico(), criarTermo(), criaConta());
//        servico = criaServico();
//        contratoDto = criaContratoDto();
//        termo = criarTermo();
//    }
//
//    @Test
//    public void retornaValorDoContrato(){
//        contrato.setValorContratado(100.00);
//        Optional<Contrato> contratoOptional = Optional.of(contrato);
//        when(contratoMapper.toEntityContratoDto(any(ContratoDto.class))).thenReturn(contrato);
//        when(contratoRepository.findByServicoAndParceiroAndConta(contrato.getServico(), contrato.getParceiro(), contrato.getConta())).thenReturn(contratoOptional);
//        Double valorContratado = contratoService.getPreco(1, 1, 1, "teste", 1);
//        assertNotNull(valorContratado);
//        assertEquals(contrato.getValorContratado(), valorContratado);
//    }
//    @Test
//    public void retornaValorDoServico(){
//        servico.setValor(100.00);
//        Optional<Servico> servicoOptional = Optional.of(servico);
//        Optional<Contrato> contratoOptional = Optional.empty();
//        when(contratoMapper.toEntityContratoDto(any(ContratoDto.class))).thenReturn(contrato);
//        when(contratoRepository.findByServicoAndParceiroAndConta(contrato.getServico(), contrato.getParceiro(), contrato.getConta())).thenReturn(contratoOptional);
//        when(servicoRepository.findByCodserv(anyInt())).thenReturn(servicoOptional);
//        Double valorContratado = contratoService.getPreco(1, 1, 1, "teste", 1);
//        assertNotNull(valorContratado);
//        assertEquals(servico.getValor(), valorContratado);
//    }
//    @Test
//    public void retornaNuloSeNaoEncontraValorDoContratoOuServico(){
//        Optional<Contrato> contratoOptional = Optional.empty();
//        Optional<Servico> servicoOptional = Optional.empty();
//        when(contratoMapper.toEntityContratoDto(any(ContratoDto.class))).thenReturn(contrato);
//        when(contratoRepository.findByServicoAndParceiroAndConta(contrato.getServico(), contrato.getParceiro(), contrato.getConta())).thenReturn(contratoOptional);
//        when(servicoRepository.findByCodserv(anyInt())).thenReturn(servicoOptional);
//        Double valorContratado = contratoService.getPreco(1, 1, 1, "teste", 1);
//        assertNull(valorContratado);
//    }
//    @Test(expected = NotFoundException.class)
//    public void retornaNotFoundExceptionEmCarroDeErro(){
//        when(contratoMapper.toEntityContratoDto(any(ContratoDto.class))).thenThrow(new RuntimeException());
//        contratoService.getPreco(1, 1, 1, "teste", 1);
//    }
//
//    @Test
//    public void salvaContrato() {
//        ContratoService contratoServiceSpy = spy(contratoService);
//        Optional<Contrato> contratoOptional = Optional.of(contrato);
//        contrato.setAudit(ModeloUtil.criaAudit());
//        when(contratoMapper.toEntityContratoDto(any(ContratoDto.class))).thenReturn(contrato);
//        when(contratoRepository.findByServicoAndParceiroAndConta(contrato.getServico(), contrato.getParceiro(), contrato.getConta())).thenReturn(contratoOptional);
//        doReturn(100.00).when(contratoServiceSpy).getPreco(anyInt(), anyInt(), anyInt(), anyString(), anyInt());
//        doReturn(contrato).when(contratoRepository).save(any(Contrato.class));
//
//        ResponseDto responseDto = contratoServiceSpy.salvaContrato(contratoDto);
//        assertNotNull(responseDto.getMessage());
//        assertEquals("Contrato salvo com sucesso.", responseDto.getMessage());
//        verify(contratoRepository, times(1)).save(any(Contrato.class));
//    }
//
//    @Test(expected = NotFoundException.class)
//    public void retornaNotFoundExceptionEmCasoDeErroAoSalvar(){
//        Optional<Contrato> contratoOptional = Optional.empty();
//        when(contratoMapper.toEntityContratoDto(any(ContratoDto.class))).thenReturn(contrato);
//        when(contratoRepository.findByServicoAndParceiroAndConta(contrato.getServico(), contrato.getParceiro(), contrato.getConta())).thenReturn(contratoOptional);
//        when(contratoRepository.save(any(Contrato.class))).thenThrow(new RuntimeException());
//        contratoService.salvaContrato(contratoDto);
//    }
//
//
//    @Test
//    public void buscaTermoAtravesDoContrato() {
//        Optional<Termo> optionalTermo = Optional.of(termo);
//        Optional<Contrato> optionalContrato = Optional.of(contrato);
//        Optional<Termo> optionalTermoContrato = Optional.of(ModeloUtil.criarTermo());
//        optionalTermoContrato.get().setVersao(0);
//
//        TermoDto termoDto = new TermoDto();
//        termoDto.setHtmlTermo(contrato.getTermo().getHtmlTermo());
//
//        when(termoRepository.findAtual()).thenReturn(optionalTermo);
//        when(contratoMapper.toEntityContratoDto(any(ContratoDto.class))).thenReturn(contrato);
//        when(contratoRepository.findByServicoAndParceiroAndContaAndAtivo(any(), any(), any(), any())).thenReturn(optionalContrato);
//        when(termoRepository.findById(anyInt())).thenReturn(optionalTermoContrato);
//        when(termoMapper.toDtoTermo(any(Termo.class))).thenReturn(termoDto);
//
//        TermoDto termoDtoResponse = contratoService.getTermo(1, 1, 1);
//        assertTrue(termoDtoResponse.isAtualizacao());
//        assertEquals(contrato.getTermo().getHtmlTermo(), termoDtoResponse.getHtmlTermo());
//    }
//
//    @Test
//    public void buscaTermoAtravesDaTabelaDeTermos() {
//        Optional<Termo> optionalTermo = Optional.of(termo);
//        Optional<Contrato> optionalContrato = Optional.empty();
//        TermoDto termoDto = new TermoDto();
//        termoDto.setHtmlTermo(contrato.getTermo().getHtmlTermo());
//        termoDto.setAtualizacao(false);
//
//        when(termoRepository.findAtual()).thenReturn(optionalTermo);
//        when(contratoMapper.toEntityContratoDto(any(ContratoDto.class))).thenReturn(contrato);
//        when(contratoRepository.findByServicoAndParceiroAndContaAndAtivo(any(), any(), any(), any())).thenReturn(optionalContrato);
//        when(termoMapper.toDtoTermo(any(Termo.class))).thenReturn(termoDto);
//
//        TermoDto termoDtoResponse = contratoService.getTermo(1, 1, 1);
//        assertFalse(termoDtoResponse.isAtualizacao());
//        assertEquals(termoDto.getHtmlTermo(), termoDtoResponse.getHtmlTermo());
//    }
//
//    @Test
//    public void naoEncontraTermonaTabelaDeTermosNemContrato() {
//        Optional<Termo> optionalTermo = Optional.empty();
//        Optional<Contrato> optionalContrato = Optional.empty();
//        TermoDto termoDto = new TermoDto();
//        termoDto.setHtmlTermo(contrato.getTermo().getHtmlTermo());
//        termoDto.setAtualizacao(false);
//
//        when(termoRepository.findAtual()).thenReturn(optionalTermo);
//        when(contratoMapper.toEntityContratoDto(any(ContratoDto.class))).thenReturn(contrato);
//        when(contratoRepository.findByServicoAndParceiroAndContaAndAtivo(any(), any(), any(), any())).thenReturn(optionalContrato);
//
//        TermoDto termoDtoResponse = contratoService.getTermo(1, 1, 1);
//        assertFalse(termoDtoResponse.isAtualizacao());
//        assertNull(termoDtoResponse.getHtmlTermo());
//    }
//
//   @Test(expected = NotFoundException.class)
//    public void retornaNotFoundExceptionEmCasoDeErroNaBuscaDoTermo(){
//       when(termoRepository.findAtual()).thenThrow(new RuntimeException());
//       contratoService.getTermo(1, 1, 1);
//    }
}

