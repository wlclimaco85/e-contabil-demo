package br.com.boleto.service;

import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.AcoesDto;
import br.com.boleto.persistence.dtos.BancoResponseDto;
import br.com.boleto.persistence.entity.Banco;
import br.com.boleto.persistence.mapper.BancoMapper;
import br.com.boleto.persistence.repository.BancoRepository;
import br.com.boleto.service.implementation.BancoService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BancoServiceTeste {

    @InjectMocks
    private BancoService service;

    @Mock
    private BancoRepository bancoRepository;

    @Mock
    private BancoMapper bancoMapper;

    private Banco banco;

    private AcoesDto bancoDto;

    @Before
    public void setUp() {
        banco = new Banco();
        bancoDto = new AcoesDto();
    }

    @Test
    public void pesquisaBancoPorIdDevolvendoUmDtoSeEncontrar() {
        banco.setId(1);
        Optional<Banco> bancoOptional = Optional.of(banco);
        when(bancoRepository.findById(anyInt())).thenReturn(bancoOptional);

        bancoDto.setId(1);
        when(bancoMapper.toDtoBanco(bancoOptional.get())).thenReturn(bancoDto);

        BancoResponseDto responseDto = service.pesquisaBancoPorId(anyInt());
        Assert.assertFalse(responseDto.getBanco().isEmpty());
        Assert.assertEquals(1, responseDto.getBanco().size());
    }

    @Test(expected = NotFoundException.class)
    public void pesquisaBancoPorIdDevolvendoUmErroSeNaoEncontrar() {
        Optional<Banco> bancoOptional = Optional.empty();
        when(bancoRepository.findById(anyInt())).thenReturn(bancoOptional);
        service.pesquisaBancoPorId(anyInt());
    }

    @Test
    public void testandoBuscaDeTodosBancosAtivos() {
        banco.setId(1);
        ArrayList<Banco> bancos = new ArrayList<>(Arrays.asList(banco));
        when(bancoRepository.findByAtivo(true)).thenReturn(bancos);

        bancoDto.setId(1);
        ArrayList<AcoesDto> bancosDto = new ArrayList<>(Arrays.asList(bancoDto));
        when(bancoMapper.toDtoListBanco(bancos)).thenReturn(bancosDto);

        BancoResponseDto responseDto = service.buscaTodosBancos();
        Assert.assertNotNull(responseDto.getBanco());
        Assert.assertEquals(1, responseDto.getBanco().size());
    }

    @Test
    public void buscaBancosNaoEncontrandoRetornaUmaListaVazia() {
        when(bancoRepository.findByAtivo(anyBoolean())).thenReturn(null);
        when(bancoMapper.toDtoListBanco(null)).thenReturn(new ArrayList<>());
        Assert.assertEquals(0, service.buscaTodosBancos().getBanco().size());
    }
}

