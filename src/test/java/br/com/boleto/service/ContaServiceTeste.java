package br.com.boleto.service;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.enums.TipoBaseEnum;
import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.ContaCredenciamentoDto;
import br.com.boleto.persistence.dtos.ContaDto;
import br.com.boleto.persistence.dtos.ContaPesquisaDto;
import br.com.boleto.persistence.dtos.ContaResponseDto;
import br.com.boleto.persistence.dtos.ContaSearchRequestDto;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.ResponseDto;
import br.com.boleto.persistence.entity.Audit;
import br.com.boleto.persistence.entity.Certificado;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.persistence.entity.ContaNaoCredenciada;
import br.com.boleto.persistence.entity.Parceiro;
import br.com.boleto.persistence.mapper.ContaMapper;
import br.com.boleto.persistence.mapper.ContaNaoCredenciadaMapper;
import br.com.boleto.persistence.repository.BoletoCustomSearchRepository;
import br.com.boleto.persistence.repository.BoletoRepository;
import br.com.boleto.persistence.repository.BoletoStatusFinalRepository;
import br.com.boleto.persistence.repository.ContaNaoCredenciadaRepository;
import br.com.boleto.persistence.repository.ContaRepository;
import br.com.boleto.service.implementation.BoletoService;
import br.com.boleto.service.implementation.ContaService;
import br.com.boleto.service.implementation.ParceiroService;
import br.com.boleto.util.DateUtil;
import br.com.boleto.util.ModeloUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ContaServiceTeste {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ContaNaoCredenciadaRepository contaNaoCredenciadaRepository;

    @Mock
    private BoletoService boletoService;

    @Mock
    private BoletoRepository boletoRepository;

    @Mock
    private BoletoStatusFinalRepository boletoStatusFinalRepository;

    @Mock
    private AuthenticationFactory authenticationFactory;

    @Spy
    private ContaNaoCredenciadaMapper contaNaoCredenciadaMapper = Mappers.getMapper(ContaNaoCredenciadaMapper.class);

    @Spy
    private ContaMapper contaMapper = Mappers.getMapper(ContaMapper.class);

    @Spy
    private HttpServletResponse responseServletResponse;

    @Mock
    private BoletoCustomSearchRepository boletoCustomSearchRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ServletOutputStream outputStream;

    @Mock
    private ParceiroService parceiroService;

    private Conta conta;

    private ContaDto contaDto;

    private ContaNaoCredenciada contaNaoCredenciada;

    private ContaCredenciamentoDto contaCredenciamentoDto;

    @Before
    public void setUp() {
        conta = ModeloUtil.criaConta();
        contaNaoCredenciada = ModeloUtil.criaContaNaoCredenciada();
        contaDto = ModeloUtil.criaContaDto();
        contaCredenciamentoDto = ModeloUtil.criaContaCredenciamentoDto();
    }

    @Test
    public void buscaTodasContasRetornandoUmArrayDeContas() {
        ArrayList<Conta> contas = new ArrayList<>();
        contas.add(conta);
        when(contaRepository.findAll()).thenReturn(contas);
        ArrayList<ContaResponseDto> contasResponseDto = contaService.getContas();
        assertFalse(contasResponseDto.isEmpty());
        assertEquals(1L, contasResponseDto.size());
    }

    @Test
    public void realizaBuscaDeContasRetornandoUmArrayVazioSeNaoEncontrarNada() {
        when(contaRepository.findAll()).thenReturn(new ArrayList<>());
        ArrayList<ContaResponseDto> contasResponseDto = contaService.getContas();
        assertTrue(contasResponseDto.isEmpty());
        assertEquals(0L, contasResponseDto.size());
    }

    @Test
    public void testanCredencimanetoDeContaSemRegistroNaTabelaDeContasNaoCredencidas() throws InterruptedException {
        ContaService contaServiceSpy = spy(contaService);
        conta.setId(1);
        conta.setRegistrobase(0);
        conta.setStatusapi("N");
        Optional<Conta> contaOpt = Optional.empty();
        Optional<ContaNaoCredenciada> contaNaoCredenciadaOpt = Optional.empty();

        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenReturn(contaOpt);
        when(contaNaoCredenciadaRepository.findByCodctaAndParceiroIdAndRegistrobase(any(), any(), any())).thenReturn(contaNaoCredenciadaOpt);
        when(contaRepository.save(any())).thenReturn(conta);
        doNothing().when(boletoService).atualizaBoletos(anyString(), anyString(), anyInt(), anyInt(), anyString());
        doNothing().when(contaServiceSpy).validaCredencial(any(ContaCredenciamentoDto.class), any());
        doNothing().when(parceiroService).salva(any(Parceiro.class));

        ResponseDto responseDto = contaServiceSpy.gerenciarCredenciamentoConta(contaCredenciamentoDto);

        assertEquals(conta.getId(), responseDto.getId());
        verify(contaRepository, times(1)).save(any());
        verify(contaServiceSpy, times(1)).validaCredencial(any(ContaCredenciamentoDto.class), any());
        Thread.sleep(2000);
        verify(boletoService, times(2)).atualizaBoletos(anyString(), anyString(), anyInt(), anyInt(), anyString());
    }

    @Test
    public void testaCredencimanetoDeContaComRegistroNaTabelaDeContasNaoCredencidas() throws InterruptedException {
        ContaService contaServiceSpy = spy(contaService);
        conta.setId(1);
        conta.setRegistrobase(1);
        Optional<Conta> contaOpt = Optional.empty();
        Optional<ContaNaoCredenciada> contaNaoCredenciadaOpt = Optional.of(contaNaoCredenciada);

        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenReturn(contaOpt);
        when(contaNaoCredenciadaRepository.findByCodctaAndParceiroIdAndRegistrobase(any(), any(), any())).thenReturn(contaNaoCredenciadaOpt);
        doNothing().when(contaNaoCredenciadaRepository).deleteByCodctaAndParceiroIdAndRegistrobase(anyInt(), anyInt(), anyInt());
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);
        doNothing().when(contaServiceSpy).validaCredencial(any(ContaCredenciamentoDto.class), any());

        ResponseDto responseDto = contaServiceSpy.gerenciarCredenciamentoConta(contaCredenciamentoDto);

        assertEquals(conta.getId(), responseDto.getId());
        verify(contaServiceSpy, times(1)).validaCredencial(any(ContaCredenciamentoDto.class), any());
        verify(contaRepository, times(1)).save(any());
    }

    @Test(expected = NotFoundException.class)
    public void testaOcorrenciaDeErrosNoCredencimaneto() {
        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenThrow(RuntimeException.class);
        contaService.gerenciarCredenciamentoConta(contaCredenciamentoDto);
    }

    @Test
    public void testaDescredenciamentoDeConta() {
        ContaService contaServiceSpy = spy(contaService);
        conta.setId(1);
        contaCredenciamentoDto.setDescredenciar(true);
        contaCredenciamentoDto.setStatusapi("N");
        Optional<Conta> contaOpt = Optional.of(conta);

        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenReturn(contaOpt);
        doNothing().when(contaRepository).descredenciaConta(any(LocalDateTime.class), anyInt());
        doNothing().when(contaServiceSpy).deleteContasCredenciadas(any(Conta.class));

        ResponseDto responseDto = contaServiceSpy.gerenciarCredenciamentoConta(contaCredenciamentoDto);

        assertEquals(conta.getId(), responseDto.getId());
        verify(contaRepository, times(1)).descredenciaConta(any(LocalDateTime.class), anyInt());
        verify(contaServiceSpy, times(1)).deleteContasCredenciadas(conta);
    }

    @Test(expected = NotFoundException.class)
    public void testaDescredenciamentoEmContaNaoExistente() {
        contaCredenciamentoDto.setDescredenciar(true);
        contaCredenciamentoDto.setStatusapi("N");
        Optional<Conta> contaOpt = Optional.empty();
        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenReturn(contaOpt);

        contaService.gerenciarCredenciamentoConta(contaCredenciamentoDto);
    }

    @Test
    public void testaAlteracaoEmContaRegistrada() {
        ContaService contaServiceSpy = spy(contaService);
        conta.setId(1);
        conta.setStatusapi("S");

        Optional<Conta> contaOpt = Optional.of(conta);
        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenReturn(contaOpt);
        when(contaRepository.save(conta)).thenReturn(conta);
        doNothing().when(contaServiceSpy).validaCredencial(any(ContaCredenciamentoDto.class), any());

        ResponseDto responseDto = contaServiceSpy.gerenciarCredenciamentoConta(contaCredenciamentoDto);
        assertEquals(conta.getId(), responseDto.getId());
        verify(contaMapper, times(1)).mergeContaCredenciamentoDtoToConta(any(), any());
        verify(contaRepository, times(1)).save(any());
    }

    @Test
    public void testaSincronizacaoDeContasCredenciadas() {
        contaDto.setStatusapi("N");
        Optional<Conta> contaOpt = Optional.of(conta);
        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenReturn(contaOpt);
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        ResponseDto responseDto = contaService.sincronizarDadosConta(contaDto);
        assertEquals(conta.getId(), responseDto.getId());
        verify(contaMapper, times(1)).mergeDtoToContaSincronizada(any(), any());
        verify(contaRepository, times(1)).save(any());
    }

    @Test(expected = NotFoundException.class)
    public void testeErroAosincronizarDadosConta() {
        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(contaDto.getCodcta(), contaDto.getIdparceiro(), contaDto.getTipobase())).thenThrow(RuntimeException.class);
        contaService.sincronizarDadosConta(contaDto);
    }

    @Test
    public void testaSincronizacaoDeContasNaoCredenciadas() {
        ContaNaoCredenciada contaNaoCredenciadaSaved = new ContaNaoCredenciada();
        contaNaoCredenciadaSaved.setId(1);
        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenReturn(Optional.empty());
        when(contaNaoCredenciadaRepository.findByCodctaAndParceiroIdAndRegistrobase(anyInt(),anyInt(), anyInt())).thenReturn(Optional.of(contaNaoCredenciada));
        when(contaNaoCredenciadaRepository.save(any(ContaNaoCredenciada.class))).thenReturn(contaNaoCredenciadaSaved);
        doNothing().when(parceiroService).salva(any(Parceiro.class));

        ResponseDto responseDto = contaService.sincronizarDadosConta(contaDto);
        assertEquals(contaNaoCredenciadaSaved.getId(), responseDto.getId());
        verify(contaNaoCredenciadaMapper, times(1)).mergeDtoToContaNaoCredenciada(any(), any());
        verify(contaNaoCredenciadaRepository, times(1)).save(any());
    }

    @Test
    public void pesquisaContaPorIdRetornaDtoSeEncontrar() {
        ContaPesquisaDto contaPesquisaDto = ModeloUtil.criaContaPesquisaDto();
         Optional<Conta> contaOptional = Optional.of(conta);
        when(contaRepository.findById(anyInt())).thenReturn(contaOptional);

        ContaPesquisaDto contaPesquisaDtoResult = contaService.pesquisaContaPorId(1);
        assertEquals(contaPesquisaDto.getId(), contaPesquisaDtoResult.getId());
        assertEquals(contaPesquisaDto.getStatusapi(), contaPesquisaDtoResult.getStatusapi());
        verify(contaMapper, times(1)).toDtoContaPesquisa(contaOptional.get());
    }

    @Test(expected = NotFoundException.class)
    public void pesquisaContaPorIdRetornaExcepectionSeNaoEncontrar() {
        Optional<Conta> contaOptional = Optional.empty();
        when(contaRepository.findById(anyInt())).thenReturn(contaOptional);
        contaService.pesquisaContaPorId(1);
    }

    @Test
    public void testaDownloadFile() throws IOException {
        ReflectionTestUtils.setField(contaService, "localChavesPEM", "src/test/resources/files");
        when(responseServletResponse.getOutputStream()).thenReturn(outputStream);
        contaService.downloadFile();
        verify(responseServletResponse).setContentType(eq("application/octet-stream"));
        verify(responseServletResponse).setHeader(eq("Content-Disposition"), eq("inline; filename=\"public.pem\""));
        verify(responseServletResponse).setContentLength(anyInt());
        verify(responseServletResponse).getOutputStream();
    }

    @Test(expected = NotFoundException.class)
    public void testeErroDownloadFile() throws IOException {
        ReflectionTestUtils.setField(contaService, "localChavesPEM", "src/test/resources/files");
        when(responseServletResponse.getOutputStream()).thenThrow(new RuntimeException());
        contaService.downloadFile();
    }

    @Test
    public void buscaContaBaseProducao() {
        ArrayList<Conta> contas = new ArrayList<>();
        contas.add(conta);
        contas.add(conta);
        when(contaRepository.findByRegistrobaseAndStatusapi(TipoBaseEnum.PRODUCAO.getTipoBase(), "S")).thenReturn(contas);

        ArrayList<ContaDto> contasDto = contaService.buscaContaBaseProducao();
        assertEquals(2, contasDto.size());
        assertEquals(contas.get(0).getId(), contasDto.get(0).getId());

        when(contaRepository.findByRegistrobaseAndStatusapi(TipoBaseEnum.PRODUCAO.getTipoBase(), "S")).thenReturn(new ArrayList<>());
        contasDto = contaService.buscaContaBaseProducao();
        assertTrue(contasDto.isEmpty());
    }

    @Test
    public void testeDeValidacaoDeCredencialSemAlteracaoEmConta() {
        ContaCredenciamentoDto contaDtoSpy = spy(contaCredenciamentoDto);
        when(contaDtoSpy.getDescredenciar()).thenReturn(true);
        contaService.validaCredencial(contaDtoSpy, conta.getId());

        when(contaDtoSpy.getDescredenciar()).thenReturn(false);
        when(contaDtoSpy.getClientid()).thenReturn("******************************");
        when(contaDtoSpy.getClientsecret()).thenReturn("******************************");
        contaService.validaCredencial(contaDtoSpy, conta.getId());

        verify(contaRepository, times(0)).alteraClientIdClientSecret(any(), any(), any());
        verify(contaRepository, times(0)).alteraStatusConta(any(), any());
    }

    @Test
    public void testeDeValicaoDeCredencialBB() {
        Optional<Conta> contaOptional = Optional.of(conta);
        Certificado certificado = ModeloUtil.criaCerticado();

        when(contaRepository.findById(conta.getId())).thenReturn(contaOptional);
        doNothing().when(contaRepository).alteraStatusConta("S", conta.getId());

        contaService.validaCredencial(contaCredenciamentoDto, conta.getId());
        verify(contaRepository, times(0)).alteraClientIdClientSecret(certificado.getClientid(),certificado.getClientsecret(),conta.getId());
        verify(contaRepository, times(1)).alteraStatusConta("S", conta.getId());
    }

    @Test
    public void testeDeValicaoDeCredencialITAU() {
        Optional<Conta> contaOptional = Optional.of(conta);
        Certificado certificado = ModeloUtil.criaCerticado();
        contaCredenciamentoDto.setCodbco(BancoEnum.ITAU.getId());

        when(authenticationFactory.getCertificado(BancoEnum.ITAU)).thenReturn(authenticationService);
        when(authenticationService.certificado(any(LoginDto.class))).thenReturn(certificado);

        when(contaRepository.findById(conta.getId())).thenReturn(contaOptional);
        doNothing().when(contaRepository).alteraClientIdClientSecret(certificado.getClientid(),certificado.getClientsecret(),conta.getId());
        doNothing().when(contaRepository).alteraStatusConta("S", conta.getId());

        contaService.validaCredencial(contaCredenciamentoDto, conta.getId());
        verify(contaRepository, times(1)).alteraClientIdClientSecret(certificado.getClientid(),certificado.getClientsecret(),conta.getId());
        verify(contaRepository, times(1)).alteraStatusConta("S", conta.getId());

        contaOptional = Optional.empty();
        when(contaRepository.findById(conta.getId())).thenReturn(contaOptional);
        contaService.validaCredencial(contaCredenciamentoDto, conta.getId());
        verify(contaRepository, times(1)).alteraClientIdClientSecret(certificado.getClientid(),certificado.getClientsecret(),conta.getId());
        verify(contaRepository, times(2)).alteraStatusConta("S", conta.getId());
    }

    @Test
    public void buscaContasConformeFiltroDaRequisicao() {
        ArrayList<Conta> contas = new ArrayList<>();
        contas.add(conta);
        contas.add(conta);
        when(boletoCustomSearchRepository.findByContaRequest(any(ContaSearchRequestDto.class))).thenReturn(contas);

        ArrayList<Conta> contasResponse = contaService.findByContaRequest(new ContaSearchRequestDto(), Pageable.unpaged());
        assertNotNull(contasResponse);
        assertEquals(contas.size(), contasResponse.size());
    }

    @Test
    @DisplayName("Bases do Tipo Produção e Não registrada devem retornar conta ATIVA")
    public void verificaContasAtivasParaCertosTipoDeBase() {
        Audit audit = new Audit();
        audit.setDataCreated(LocalDateTime.now());
        audit.setDataUpdated(LocalDateTime.of(1900,1,1,0,0));
        contaDto.setAudit(audit);
        contaDto.setTipobase(TipoBaseEnum.PRODUCAO.getTipoBase());
        contaDto.setStatusapi("S");

        assertFalse(contaService.estaInativa(contaDto));
        contaDto.setTipobase(TipoBaseEnum.NAO_REGISTRADA.getTipoBase());
        assertFalse(contaService.estaInativa(contaDto));
    }

    @Test
    @DisplayName("Quando buscar boletos por conta e não retornar nenhum boleto deve retornar conta INATIVA")
    public void verificaContaInativaComNenhumRetornoDeBoletos() {
        Audit audit = new Audit();
        audit.setDataCreated(DateUtil.getDataAtual());
        audit.setDataUpdated(LocalDateTime.of(1900,1,1,0,0));
        contaDto.setId(1);
        contaDto.setAudit(audit);
        contaDto.setTipobase(TipoBaseEnum.PRODUCAO.getTipoBase());
        contaDto.setStatusapi("S");
        List<BoletoDto> boletoDtos = new ArrayList<>();
        when(boletoService.buscaPorConta(contaDto.getId())).thenReturn(boletoDtos);

        contaDto.setTipobase(TipoBaseEnum.TESTE.getTipoBase());
        boolean isInativa = contaService.estaInativa(contaDto);

        assertTrue(isInativa);
        verify(boletoService, times(1)).buscaPorConta(anyInt());
        verify(boletoService, times(0)).buscaUltimaDataAtualizacaoPorConta(anyInt());
    }

    @Test
    @DisplayName("Deve retornar conta INATIVA caso encontre mais de um boleto e a última data de atualização + 7 dias seja MENOR que a data atual")
    public void contaDeveEstarInativaComUltimaAtualizacaoBoletoMaisSeteDiasAntesDaDataAtual() {
        Audit audit = new Audit();
        audit.setDataCreated(DateUtil.getDataAtual());
        LocalDateTime dtAtualizacao = LocalDateTime.of(1900, 1, 1, 0, 0);
        audit.setDataUpdated(dtAtualizacao);
        contaDto.setId(1);
        contaDto.setAudit(audit);
        contaDto.setTipobase(TipoBaseEnum.PRODUCAO.getTipoBase());
        contaDto.setStatusapi("S");
        List<BoletoDto> boletoDtos = Arrays.asList(new BoletoDto(), new BoletoDto());
        when(boletoService.buscaPorConta(contaDto.getId())).thenReturn(boletoDtos);
        when(boletoService.buscaUltimaDataAtualizacaoPorConta(contaDto.getId())).thenReturn(dtAtualizacao);

        contaDto.setTipobase(TipoBaseEnum.TESTE.getTipoBase());
        boolean isInativa = contaService.estaInativa(contaDto);

        assertTrue(isInativa);
        verify(boletoService, times(1)).buscaPorConta(anyInt());
        verify(boletoService, times(1)).buscaUltimaDataAtualizacaoPorConta(anyInt());

    }

    @Test
    @DisplayName("Deve retornar conta ATIVA caso encontre mais de um boleto e a última data de atualização + 7 dias seja MAIOR que a data atual")
    public void contaDeveEstarAtivaComUltimaAtualizacaoBoletoMaisSeteDiasAntesDaDataAtual() {
        Audit audit = new Audit();
        audit.setDataCreated(DateUtil.getDataAtual());
        LocalDateTime dtAtualizacao = LocalDateTime.of(1900, 1, 1, 0, 0);
        audit.setDataUpdated(dtAtualizacao);
        contaDto.setId(1);
        contaDto.setAudit(audit);
        contaDto.setTipobase(TipoBaseEnum.PRODUCAO.getTipoBase());
        contaDto.setStatusapi("S");
        List<BoletoDto> boletoDtos = Arrays.asList(new BoletoDto(), new BoletoDto());
        when(boletoService.buscaPorConta(contaDto.getId())).thenReturn(boletoDtos);
        when(boletoService.buscaUltimaDataAtualizacaoPorConta(contaDto.getId())).thenReturn(DateUtil.getDataAtual());

        contaDto.setTipobase(TipoBaseEnum.TESTE.getTipoBase());
        boolean isInativa = contaService.estaInativa(contaDto);

        assertFalse(isInativa);
        verify(boletoService, times(1)).buscaPorConta(anyInt());
        verify(boletoService, times(1)).buscaUltimaDataAtualizacaoPorConta(anyInt());

    }

    @Test
    @DisplayName("Deve retornar conta inativa caso encontre apenas um boleto e a data de atualização + 7 dias seja MENOR que a data atual")
    public void contaDeveEstarInativaComUltimaAtualizacaoAuditBoletoMaisSeteDiasAntesDaDataAtual() {
        Audit audit = new Audit();
        audit.setDataCreated(DateUtil.getDataAtual());
        audit.setDataUpdated(LocalDateTime.of(1900, 1, 1, 0, 0));
        contaDto.setId(1);
        contaDto.setAudit(audit);
        contaDto.setTipobase(TipoBaseEnum.PRODUCAO.getTipoBase());
        contaDto.setStatusapi("S");

        Audit audit2 = new Audit();
        audit2.setDataCreated(DateUtil.getDataAtual());
        audit2.setDataUpdated(LocalDateTime.of(1900, 1, 1, 0, 0));
        BoletoDto boletoDto = new BoletoDto();
        boletoDto.setAudit(audit2);

        List<BoletoDto> boletoDtos = List.of(boletoDto);
        when(boletoService.buscaPorConta(contaDto.getId())).thenReturn(boletoDtos);

        contaDto.setTipobase(TipoBaseEnum.TESTE.getTipoBase());
        boolean isInativa = contaService.estaInativa(contaDto);

        assertTrue(isInativa);
        verify(boletoService, times(1)).buscaPorConta(anyInt());

    }

    @Test
    public void deletandoRegistrosDeContasCredenciadasSemBoleto() {
        ContaNaoCredenciada contaNaoCredenciadaReturn = new ContaNaoCredenciada();
        contaNaoCredenciadaReturn.setId(1);

        when(boletoRepository.existsByContaId(any())).thenReturn(false);
        when(boletoStatusFinalRepository.existsByContaId(any())).thenReturn(false);
        doNothing().when(contaRepository).delete(any(Conta.class));
        doNothing().when(contaNaoCredenciadaRepository).deleteByCodctaAndParceiroIdAndRegistrobase(any(), any(), any());

        contaService.deleteContasCredenciadas(conta);
        verify(contaRepository, times(1)).delete(conta);
        verify(contaNaoCredenciadaRepository, times(1)).deleteByCodctaAndParceiroIdAndRegistrobase(any(), any(), any());
        verify(contaNaoCredenciadaRepository, times(1)).save(any());
    }

    @Test
    public void deletandoRegistrosDeContasCredenciadasComBoleto() {
        when(boletoRepository.existsByContaId(any())).thenReturn(true);
        when(boletoStatusFinalRepository.existsByContaId(any())).thenReturn(true);

        contaService.deleteContasCredenciadas(conta);
        verify(contaRepository, times(0)).delete(conta);
        verify(contaNaoCredenciadaRepository, times(0)).deleteByCodctaAndParceiroIdAndRegistrobase(any(), any(), any());
        verify(contaNaoCredenciadaRepository, times(0)).save(any());
    }

    @Test
    public void provocandoErroNoDeleteDeContasCredenciadas() {
        doThrow(NotFoundException.class).when(contaRepository).delete(conta);
        contaService.deleteContasCredenciadas(conta);
    }

    @Test
    @DisplayName("Faz o teste de exclusão de contas não credenciadas e após deleta ou descredencia contas credenciadas")
    public void deletandoRegistrosDeContasQualquerTipoDeConta() {
        Optional<Conta> contaOpt = Optional.empty();
        contaDto.setId(1);
        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenReturn(contaOpt);

        ResponseDto responseDto = contaService.deleteConta(contaDto);
        assertEquals(contaDto.getId(), responseDto.getId());
        verify(contaRepository, times(0)).descredenciaConta(any(LocalDateTime.class), any());
        verify(contaNaoCredenciadaRepository, times(1)).deleteByCodctaAndParceiroIdAndRegistrobase(anyInt(), anyInt(), anyInt());

        reset(contaRepository);
        reset(contaNaoCredenciadaRepository);
        ContaService contaServiceSpy = spy(contaService);

        contaOpt = Optional.of(conta);
        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenReturn(contaOpt);
        doNothing().when(contaServiceSpy).deleteContasCredenciadas(any());

        responseDto = contaServiceSpy.deleteConta(contaDto);
        assertEquals(contaDto.getId(), responseDto.getId());
        verify(contaRepository, times(1)).descredenciaConta(any(LocalDateTime.class), any());
        verify(contaNaoCredenciadaRepository, times(0)).deleteByCodctaAndParceiroIdAndRegistrobase(anyInt(), anyInt(), anyInt());
        verify(contaServiceSpy, times(1)).deleteContasCredenciadas(any());
    }

    @Test(expected =  NotFoundException.class)
    public void provocandoEerroNoDeleteDeContasQualquerTipoDeConta() {
        when(contaRepository.buscarContaIdParceiroCodCtaRegistroBase(any(), any(), any())).thenThrow(RuntimeException.class);
        contaService.deleteConta(contaDto);
    }

}

