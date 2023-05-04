package br.com.boleto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;

import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.enums.TipoEventoEnum;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.LogEnvioDto;
import br.com.boleto.persistence.dtos.LogEnvioResponseDto;
import br.com.boleto.persistence.dtos.LogEnvioSearchRequestDto;
import br.com.boleto.persistence.entity.LogEnvio;
import br.com.boleto.persistence.repository.BoletoCustomSearchRepository;
import br.com.boleto.persistence.repository.LogEnvioRepository;
import br.com.boleto.service.implementation.BoletoService;
import br.com.boleto.service.implementation.LogEnvioService;
import br.com.boleto.util.BoletoUtil;
import br.com.boleto.util.ModeloUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class LogEnvioServiceTeste {

    @InjectMocks
    private LogEnvioService logEnvioService;

    @Mock
    private LogEnvioRepository logEnvioRepository;

    @Mock
    BoletoCustomSearchRepository boletoCustomSearchRepository;

    @Mock
    private BoletoService boletoService;

    private LogEnvio logEnvio;

    private LogEnvioDto logEnvioDto;

    private BoletoDto boletoDto;

    @Before
    public void setUp() {
        logEnvio = ModeloUtil.criarLogEnvio();
        logEnvioDto = ModeloUtil.criaLogEnvioDto();
        boletoDto = BoletoUtil.criaBoletoDto().get(0);
    }

    @Test
    public void testandoSalvardoLogEnvio() {
        when(logEnvioRepository.save(any(LogEnvio.class))).thenReturn(logEnvio);
        logEnvioService.salvar(logEnvioDto);
        verify(logEnvioRepository, times(1)).save(any(LogEnvio.class));
    }

    @Test
    public void seNaoEncontrarLogPeloNossoNumeroRetornaListaVazia() {
        when(logEnvioRepository.findByNossonumeroOrderByDhocorrenciaAsc(anyString())).thenReturn(new ArrayList<>());
        ArrayList<LogEnvioResponseDto> result = logEnvioService.findByNumeroTituloCliente("123", 0);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
    }

    @Test
    public void buscaListaDeLogEnvio() {
        ArrayList<LogEnvio> logEnvioList = new ArrayList<>();
        LogEnvio log1 = ModeloUtil.criarLogEnvio();
        LogEnvio log2 = ModeloUtil.criarLogEnvio();
        LogEnvio log3 = ModeloUtil.criarLogEnvio();

        log1.setStatus(StatusBoletoEnum.ERRO);

        logEnvioList.add(log1);
        logEnvioList.add(log2);
        logEnvioList.add(log3);

        when(logEnvioRepository.findByNossonumeroOrderByDhocorrenciaAsc(anyString())).thenReturn(logEnvioList);

        ArrayList<LogEnvioResponseDto> result = logEnvioService.findByNumeroTituloCliente("123", 0);
        assertEquals(3, result.size());
        assertEquals(log1.getId(), result.get(0).getLogEnvio().getId());
        assertEquals(log1.getNossonumero(), result.get(0).getLogEnvio().getNossonumero());
        assertEquals(log1.getTipoEvento().getTipoEvento(), result.get(0).getLogEnvio().getTipoEvento());
    }

    @Test
    public void buscaDeLogEnvioPorNumeroEStackTraceNaoEncontraERetornaNu() {
        Optional<LogEnvio> logEnvioOptional = Optional.empty();
        when(logEnvioRepository.findByNossonumeroAndStacktrace(anyString(), anyString())).thenReturn(logEnvioOptional);

        LogEnvioDto result = logEnvioService.findByNossonumeroAndStacktrace("123", "stacktrace");
        assertNull(result);
    }

    @Test
    public void buscaDeLogEnvioPeloNumeroDoTituloEStackTrace() {

        Optional<LogEnvio> logEnvioOptional = Optional.of(logEnvio);
        when(logEnvioRepository.findByNossonumeroAndStacktrace(anyString(), anyString())).thenReturn(logEnvioOptional);

        LogEnvioDto result = logEnvioService.findByNossonumeroAndStacktrace("123", "stacktrace");
        assertEquals(logEnvio.getId(), result.getId());
        assertEquals(logEnvio.getNossonumero(), result.getNossonumero());
        assertEquals(logEnvio.getMensagem(), result.getMensagem());
        assertEquals(logEnvio.getStacktrace(), result.getStacktrace());
    }

    @Test
    public void testaSaveDoLogEnvioEAlteracaoBoleto() {
        LogEnvioService logEnvioServiceSpy = spy(logEnvioService);
        doReturn(logEnvioDto).when(logEnvioServiceSpy).findByNossonumeroAndStacktrace(anyString(), anyString());
        when(boletoService.alteraBoletosRetornoBanco(any())).thenReturn(null);
        doNothing().when(logEnvioServiceSpy).salvar(any(LogEnvioDto.class));

        logEnvioServiceSpy.insereRetornoBancoLogEnvio(boletoDto, new Exception("Erro"), "", "Desconto", "TESTE", TipoEventoEnum.BAIXA_CANCELAMENTO);
        verify(boletoService, times(1)).alteraBoletosRetornoBanco(any());
        verify(logEnvioServiceSpy, times(1)).salvar(any(LogEnvioDto.class));

        logEnvioServiceSpy.insereRetornoBancoLogEnvio(boletoDto, new Exception("Erro"), "", "Desconto", "TESTE", TipoEventoEnum.ALTERACAO);
        verify(boletoService, times(2)).alteraBoletosRetornoBanco(any());
        verify(logEnvioServiceSpy, times(2)).salvar(any(LogEnvioDto.class));
    }

    @Test
    public void testaBuscaPorLogEnvio() {
        List<LogEnvio> logEnvioList = new ArrayList<>();
        logEnvioList.add(logEnvio);
        logEnvioList.add(logEnvio);
        when(boletoCustomSearchRepository.findByLogEnvioRequest(any(LogEnvioSearchRequestDto.class))).thenReturn(logEnvioList);
        ArrayList<LogEnvio> result = logEnvioService.findByLogEnvioRequest(new LogEnvioSearchRequestDto(), Pageable.unpaged());
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(logEnvioList.get(0).getId(), result.get(0).getId());
    }
    public ArrayList<LogEnvio> findByLogEnvioRequest(LogEnvioSearchRequestDto filter, Pageable pageable){
        return (ArrayList<LogEnvio>) boletoCustomSearchRepository.findByLogEnvioRequest(filter);
    }

}

