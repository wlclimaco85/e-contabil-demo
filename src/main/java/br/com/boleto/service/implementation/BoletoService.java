package br.com.boleto.service.implementation;

import static java.util.Objects.isNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.enums.JobEnum;
import br.com.boleto.enums.SituacaoEnum;
import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.enums.StatusBoletoItauEnum;
import br.com.boleto.enums.TipoEventoEnum;
import br.com.boleto.exception.BadRequestException;
import br.com.boleto.exception.FailedAuthenticationException;
import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.AcaoRetornoDto;
import br.com.boleto.persistence.dtos.AcaoRetornoDto4;
import br.com.boleto.persistence.dtos.AcaoRetornoDto5;
import br.com.boleto.persistence.dtos.AtualizaBoletosRequestDto;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.BoletoPaginadosDto;
import br.com.boleto.persistence.dtos.BoletoPaginadosFilterRequestDto;
import br.com.boleto.persistence.dtos.BoletoResponseDto;
import br.com.boleto.persistence.dtos.BoletoRetornoDto;
import br.com.boleto.persistence.dtos.CancelamentoBoletoResponseDto;
import br.com.boleto.persistence.dtos.ListagemBoletoResponseDto;
import br.com.boleto.persistence.dtos.ListagemBoletosResponseDto;
import br.com.boleto.persistence.dtos.LogEnvioDto;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.LoginResponseDto;
import br.com.boleto.persistence.dtos.RegistroBoletoResponseDto;
import br.com.boleto.persistence.dtos.StatusBoletoFilterRequestDto;
import br.com.boleto.persistence.dtos.StatusBoletoFilterSearchRequestDto;
import br.com.boleto.persistence.dtos.StatusBoletoResponseDto;
import br.com.boleto.persistence.dtos.StatusContaRequestDto;
import br.com.boleto.persistence.dtos.StatusRequestDto;
import br.com.boleto.persistence.entity.Acoes;
import br.com.boleto.persistence.entity.Audit;
import br.com.boleto.persistence.entity.Boleto;
import br.com.boleto.persistence.entity.BoletoRetorno;
import br.com.boleto.persistence.entity.BoletoStatusFinal;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.persistence.entity.Job;
import br.com.boleto.persistence.entity.LogEnvio;
import br.com.boleto.persistence.mapper.BoletoMapper;
import br.com.boleto.persistence.repository.AcoesRepository;
import br.com.boleto.persistence.repository.BoletoCustomRepository;
import br.com.boleto.persistence.repository.BoletoCustomSearchRepository;
import br.com.boleto.persistence.repository.BoletoRepository;
import br.com.boleto.persistence.repository.BoletoRetornoRepository;
import br.com.boleto.persistence.repository.BoletoStatusFinalRepository;
import br.com.boleto.persistence.repository.ContaRepository;
import br.com.boleto.persistence.repository.JobRepository;
import br.com.boleto.persistence.repository.LogEnvioRepository;
import br.com.boleto.queues.AlteraBoletoQueue;
import br.com.boleto.queues.CancelaBoletoQueue;
import br.com.boleto.queues.ConsultaBoletoQueue;
import br.com.boleto.queues.RegistraBoletoQueue;
import br.com.boleto.service.AuthenticationFactory;
import br.com.boleto.service.BoletoProcessorFactory;
import br.com.boleto.util.Constantes;
import br.com.boleto.util.DateUtil;
import br.com.boleto.util.GetRetornoErro;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoletoService {
    @Autowired
    private BoletoRepository boletoRepository;

    @Autowired
    private LogEnvioRepository logEnvioRepository;
    
    @Autowired
    private AcoesRepository acoesRepository;

    @Autowired
    private BoletoCustomRepository boletoCustomRepository;
    
    @Autowired
    private BoletoStatusFinalRepository boletoStatusFinalRepository;


    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private LogEnvioService logEnvioService;

    @Autowired
    private BoletoRetornoRepository boletoRetornoRepository;
    
    @Autowired
    private AuthenticationFactory authenticationFactory;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    BoletoCustomSearchRepository boletoCustomSearchRepository;

    @Autowired
    private BoletoProcessorFactory boletoProcessorFactory;

    @Autowired
    private BoletoMapper boletoMapper;

    @Value("${url.api.bb.cobranca}")
    private String urlApiBbCobranca;

    @Value("${gw-dev-app-key}")
    private String gwDevAppKey;

	public ArrayList<BoletoResponseDto> getStatus(ArrayList<StatusRequestDto> boletoDtos) {
		/* TODO
		O Sankhya deve ter o job alterado para rodar a consulta de 1 hr em hr
		
		O MS deve ter uma lista contendo o numeroConta e o ArrayResponse
		Caso a conta buscada nao esteja na lista ainda será feito uma recarga no banco 
		com base nos nufins consultados.
		
		A lista sera recarregada se durante a consulta com o BB algum boleto se tonar
		baixado
		*/
		ArrayList<BoletoResponseDto> boletoDtolist = new ArrayList<>();

		try {

			// para melhorar por hora esse servico vai restringir o acesso a lista somente durante algumas faixas de tempo.
			if (DateUtil.canExecute()) {

				for (StatusRequestDto status : boletoDtos) {
					List<Boleto> boletolist = boletoRepository.findDistinctByStatusIn(status.getStatus(), status.getNufins(), status.getIpApiBanco());

					if (!boletolist.isEmpty()) {
						for (Boleto boleto : boletolist) {
							BoletoResponseDto boletoDtod = new BoletoResponseDto();
							BoletoDto boletoDto = new BoletoDto(boleto);
							boletoDtod.setBoleto(boletoDto);
							boletoDtolist.add(boletoDtod);
						}
					}
				}
			}

			return boletoDtolist;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NotFoundException("Erro ao buscar status");
		}

	}

    public ArrayList<BoletoResponseDto> getEmvpixBoleto(ArrayList<BoletoDto> boletoDtos) {
        ArrayList<BoletoResponseDto> boletoDtolist = new ArrayList<>();
        try {
            for (BoletoDto boletos : boletoDtos) {
                if (boletos.getNumeroTituloCliente() != null) {
                    Boleto boleto = boletoRepository.findBynumeroTituloCliente(boletos.getNumeroTituloCliente());
                    if (boleto != null) {
                        BoletoResponseDto boletoDtod = new BoletoResponseDto();
                        BoletoDto boletoDto = new BoletoDto(boleto);
                        boletoDtod.setBoleto(boletoDto);
                        boletoDtolist.add(boletoDtod);
                    }
                }
            }
            return boletoDtolist;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NotFoundException("Erro ao buscar EmvPixBoleto");
        }
    }

    public ArrayList<BoletoResponseDto> getBoletoSemNufin() {
        List<Boleto> boletolist = boletoRepository.findByNumeroTituloBeneficiarioIsNull();

        ArrayList<BoletoResponseDto> boletoDtolist = new ArrayList<>();

        if (!boletolist.isEmpty()) {
            for (Boleto boleto : boletolist) {
                BoletoResponseDto boletoDtod = new BoletoResponseDto();
                BoletoDto boletoDto = new BoletoDto(boleto);

                if (boleto.getConta().getId() != null && boleto.getConta().getId() > 0) {
                    Optional<Conta> conta = contaRepository.findById(boleto.getConta().getId());
                    if (conta != null && conta.isPresent()) {
                        boletoDto.setId(boleto.getId());
                        boletoDto.setIdapibanco(boleto.getConta().getId());
                        boletoDto.setNumeroTituloBeneficiario(boleto.getNumeroTituloBeneficiario());
                        boletoDto.setNumeroTituloCliente(boleto.getNumeroTituloCliente());
                        boletoDtod.setCodcta(conta.get().getCodcta());
                        boletoDtod.setBoleto(boletoDto);
                        boletoDtolist.add(boletoDtod);
                    }
                }
            }
        }

        return boletoDtolist;
    }

    public BoletoDto findByNumeroTituloBeneficiario(Integer numeroTituloBeneficiario) {
        Boleto boleto = boletoRepository.findByNumeroTituloBeneficiario(numeroTituloBeneficiario);
        if (boleto != null) {
            BoletoDto boletoDto = new BoletoDto(boleto);
            return boletoDto;
        }
        return new BoletoDto();
    }

    public List<BoletoDto> buscaPorStatus(Integer status, Integer contaPendente) {
        List<Boleto> boletolist = new ArrayList<Boleto>();
        if (StatusBoletoEnum.ALTERA_BOLETO.getStatus().equals(status) || StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus().equals(status)) {
            Date date = new Date();
            Timestamp dataAtual = new Timestamp(date.getTime());
            boletolist = boletoRepository.findByStatus(status, dataAtual, contaPendente);
        } else {
            boletolist = boletoRepository.findByStatusAndConta(status, contaPendente);
        }
        List<BoletoDto> boletoDtolist = new ArrayList<BoletoDto>();

        if (!boletolist.isEmpty()) {
            for (Boleto boleto : boletolist) {
                Optional<Conta> conta = contaRepository.findById(boleto.getConta().getId());

                if (conta != null && conta.isPresent() && "S".equals(conta.get().getStatusapi())) {
                    BoletoDto boletoDto = new BoletoDto(boleto);
                    boletoDto.setStatusBanco(verificaStatusBanco(status));
                    boletoDtolist.add(boletoDto);
                }
            }
        }

        return boletoDtolist;
    }

	public ArrayList<BoletoResponseDto> processaBoletos(ArrayList<BoletoDto> boletoDtos) {

		ArrayList<BoletoResponseDto> response = new ArrayList<>();

		if (boletoDtos.isEmpty() || (!boletoDtos.isEmpty() && !verificaAutenticidadeConta(boletoDtos.get(0).getIdapibanco()))) {
			throw new RuntimeException("Não foi possivel processar a lista de Boletos");
		}

		for (BoletoDto boletoDto : boletoDtos) {

			try {
				Boleto boleto = boletoRepository.findBynumeroTituloCliente(boletoDto.getNumeroTituloCliente());
				Boolean isInserir = (boleto == null) ? true : false;
				Boleto saved = (isInserir) ? insereBoleto(boletoDto) : atualizaBoleto(boletoDto, boleto);
				BoletoResponseDto item = new BoletoResponseDto(saved.getId(), boletoDto, "Boleto Processado com sucesso!", isInserir);
				response.add(item);

			} catch (Exception e) {
				log.error("Erro ao salvar boleto - Número Título Cliente: {} [Método: salvarAlterar]",
						isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
				log.error(e.getMessage(), e);
				BoletoResponseDto item = new BoletoResponseDto(null, boletoDto, e.getMessage(), false);
				response.add(item);
			}
		}

		return response;
	}
	
	public Boolean verificaAutenticidadeConta(Integer idconta) {
    	Optional<Conta> optConta = contaRepository.findById(idconta);
        if (!optConta.isEmpty() && optConta.isPresent()) {
            Conta conta = optConta.get();
            LoginResponseDto loginResponseDto = authentication(conta);
            return (loginResponseDto.getAccess_token() != null) ? true : false;
        }
        
       return false;
    }

    @Transactional
    public Boleto insereBoleto(BoletoDto boleto) {
    	
    	try {
    		Boleto newBoleto = new Boleto(boleto);

            newBoleto.setIndicadorNovaDataVencimento("N");
            newBoleto.setIndicadorAtribuirDesconto("N");
            newBoleto.setIndicadorAlterarDesconto("N");
            newBoleto.setIndicadorAlterarDataDesconto("N");
            newBoleto.setIndicadorProtestar("N");
            newBoleto.setIndicadorSustacaoProtesto("N");
            newBoleto.setIndicadorCancelarProtesto("N");
            newBoleto.setIndicadorIncluirAbatimento("N");
            newBoleto.setIndicadorCancelarAbatimento("N");
            newBoleto.setIndicadorCobrarJuros("N");
            newBoleto.setIndicadorDispensarJuros("N");
            newBoleto.setIndicadorCobrarMulta("N");
            newBoleto.setIndicadorDispensarMulta("N");
            newBoleto.setIndicadorNegativar("N");
            newBoleto.setIndicadorAlterarNossoNumero("N");
            newBoleto.setIndicadorAlterarEnderecoPagador("N");
            newBoleto.setIndicadorAlterarPrazoBoletoVencido("N");
            newBoleto.setStatusBanco(StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus());
            newBoleto.setQuantidadeTentativas(0);
            
            Integer codBanco = contaRepository.buscarIdConta(newBoleto.getConta().getId());
            if(newBoleto.getNumeroTituloCliente().length() > 20 && BancoEnum.isBancoBrasil(codBanco) ) {
            	newBoleto.setStatus(StatusBoletoEnum.ERRO.getStatus());
            	LogEnvioDto logEnvioDto = new LogEnvioDto(newBoleto.getNumeroTituloCliente(),
            			TipoEventoEnum.ERRO_VALIDACAO_BOLETO.getDescricaoEvento(), "Tamanho do nosso número inválido",
            			StatusBoletoEnum.ERRO.getStatus(),
            			TipoEventoEnum.ERRO_VALIDACAO_BOLETO.getIdTipoEvento(),
            			SituacaoEnum.REJEITADA.getIdSituacao(),
            			StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus());
            	logEnvioService.salvar(logEnvioDto);
            	return boletoRepository.save(newBoleto);
            } 
            
        	LogEnvioDto logEnvioDto = new LogEnvioDto(newBoleto.getNumeroTituloCliente(),
        			TipoEventoEnum.EMISSAO.getDescricaoEvento(), "", boleto.getStatus(),
        			TipoEventoEnum.EMISSAO.getIdTipoEvento(),
        			SituacaoEnum.PENDENTE_ENVIO.getIdSituacao(),
        			StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus());
        	logEnvioService.salvar(logEnvioDto);
        	
            return boletoRepository.save(newBoleto);
            
    	}catch (Exception e) {
            log.error("Erro ao salvar boleto - Número Título Cliente: {} [Método: salvarAlterar]", isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
            log.error(e.getMessage(), e);
            throw new NotFoundException("Erro ao salvar boleto");
        }	
    }
    
   
    @Transactional
    public Boleto atualizaBoleto(BoletoDto boleto, Boleto boletoOpt) {
    	if(isStatusFinal(boletoOpt.getStatusBanco().getStatus(), boletoOpt.getConta().getBanco().getId())){
    		logEnvioService.insereLogEnvioBoletoEmStatusFinal(boletoOpt);
    		return boletoOpt;
		}
    	
    	try {
    		boletoOpt.setStatusBanco(verificaStatusBanco(boletoOpt.getStatus().getStatus()));

            if (boleto.getStatus() == null || boleto.getStatus() == 0) {
                boleto.setStatus(boletoOpt.getStatus().getStatus());
            }
            if (boletoOpt.getNumeroTituloBeneficiario() == null) {
                boletoOpt.setNumeroTituloBeneficiario(boleto.getNumeroTituloBeneficiario());
                return boletoRepository.save(boletoOpt);
            }
            
            Boleto newBoleto = boletoOpt;
            boleto.setId(boletoOpt.getId());
            atualizaDependencias(boleto, boletoOpt);
        	BoletoDto boletoAlt = alteracaoBoleto(boleto);
        	newBoleto = new Boleto(boletoAlt);
            Boleto saved = boletoRepository.save(newBoleto);

            if (!StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus().equals(boleto.getStatus())) {
                LogEnvioDto logEnvioDto = new LogEnvioDto(boleto.getNumeroTituloCliente(),
                        TipoEventoEnum.ALTERACAO.getDescricaoEvento(), "", boleto.getStatus(),
                        TipoEventoEnum.ALTERACAO.getIdTipoEvento(),
                        SituacaoEnum.PENDENTE_ENVIO.getIdSituacao(),
                        StatusBoletoEnum.NORMAL.getStatus());
                logEnvioService.salvar(logEnvioDto);
            }
            
            return saved;
            
    	}catch (Exception e) {
            log.error("Erro ao salvar boleto - Número Título Cliente: {} [Método: salvarAlterar]", isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
            log.error(e.getMessage(), e);
            throw new NotFoundException("Erro ao salvar boleto");
        }	
    }
    
    public void atualizaDependencias(BoletoDto boletoDto, Boleto boleto) {
    	if (!isNull(boletoDto.getDesconto()) && !isNull(boleto.getDesconto())) {
    		boletoDto.getDesconto().setId(boleto.getDesconto().getId());
        }
        if (!isNull(boletoDto.getSegundoDesconto()) && !isNull(boleto.getSegundoDesconto())) {
        	boletoDto.getSegundoDesconto().setId(boleto.getSegundoDesconto().getId());
        }
        if (!isNull(boletoDto.getTerceiroDesconto()) && !isNull(boleto.getTerceiroDesconto())) {
        	boletoDto.getTerceiroDesconto().setId(boleto.getTerceiroDesconto().getId());
        }
        if (!isNull(boletoDto.getMulta()) && !isNull(boleto.getMulta())) {
        	boletoDto.getMulta().setId(boleto.getMulta().getId());
        }
        if (!isNull(boletoDto.getJurosMora()) && !isNull(boleto.getJurosMora())) {
        	boletoDto.getJurosMora().setId(boleto.getJurosMora().getId());
        }
        if (!isNull(boletoDto.getPagador()) && !isNull(boleto.getPagador())) {
        	boletoDto.getPagador().setId(boleto.getPagador().getId());
        }
        if (!isNull(boletoDto.getBeneficiarioFinal()) && !isNull(boleto.getBeneficiarioFinal())) {
        	boletoDto.getBeneficiarioFinal().setId(boleto.getBeneficiarioFinal().getId());
        }
    }
    
    @Transactional
    public ArrayList<BoletoResponseDto> alteraBoletosRetornoBanco(ArrayList<BoletoDto> boletoDtos) {
        ArrayList<BoletoResponseDto> response = new ArrayList<>();

        if (boletoDtos != null && !boletoDtos.isEmpty()) {
            Conta conta = contaRepository.findById(boletoDtos.get(0).getIdapibanco()).get();
            LoginResponseDto loginResponseDto = authentication(conta);

            if (loginResponseDto.getAccess_token() != null) {
                for (BoletoDto boleto : boletoDtos) {
                    BoletoResponseDto item = new BoletoResponseDto();
                    Boleto boletoOpt = boletoRepository.findBynumeroTituloCliente(boleto.getNumeroTituloCliente());

                    try {
                        if (boletoOpt == null) {
                            Boleto newBoleto = new Boleto(boleto);

                            newBoleto.setIndicadorNovaDataVencimento("N");
                            newBoleto.setIndicadorAtribuirDesconto("N");
                            newBoleto.setIndicadorAlterarDesconto("N");
                            newBoleto.setIndicadorAlterarDataDesconto("N");
                            newBoleto.setIndicadorProtestar("N");
                            newBoleto.setIndicadorSustacaoProtesto("N");
                            newBoleto.setIndicadorCancelarProtesto("N");
                            newBoleto.setIndicadorIncluirAbatimento("N");
                            newBoleto.setIndicadorCancelarAbatimento("N");
                            newBoleto.setIndicadorCobrarJuros("N");
                            newBoleto.setIndicadorDispensarJuros("N");
                            newBoleto.setIndicadorCobrarMulta("N");
                            newBoleto.setIndicadorDispensarMulta("N");
                            newBoleto.setIndicadorNegativar("N");
                            newBoleto.setIndicadorAlterarNossoNumero("N");
                            newBoleto.setIndicadorAlterarEnderecoPagador("N");
                            newBoleto.setIndicadorAlterarPrazoBoletoVencido("N");
                            newBoleto.setStatusBanco(StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus());
                            newBoleto.setQuantidadeTentativas(0);
                            
                            Boleto saved = boletoRepository.save(newBoleto);

                            if (saved.getId() != null) {
                                item.setId(saved.getId());
                                item.setBoleto(boleto);
                                item.setType(true);
                                item.setMessage("Boleto inserido com sucesso!");
                            } else {
                                log.error("Erro ao salvar boleto - Número Título Cliente: {} [Método: alterar]", isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
                                throw new RuntimeException("Erro ao salvar boleto");
                            }
                        } else {
                        	boletoOpt.setStatusBanco(verificaStatusBanco(boletoOpt.getStatus().getStatus()));

                            if (boleto.getStatus() == null || boleto.getStatus() == 0) {
                                boleto.setStatus(boletoOpt.getStatus().getStatus());
                            }
                            if (boletoOpt.getNumeroTituloBeneficiario() == null) {
                                boletoOpt.setNumeroTituloBeneficiario(boleto.getNumeroTituloBeneficiario());
                                Boleto saved = boletoRepository.save(boletoOpt);
                                BoletoDto newBoleto = new BoletoDto(boletoOpt);
                                item.setId(saved.getId());
                                item.setBoleto(newBoleto);
                            } else {
                                Boleto newBoleto = boletoOpt;
                                boleto.setId(boletoOpt.getId());
                                newBoleto = new Boleto(boleto);
                                Boleto saved = boletoRepository.save(newBoleto);
                                item.setId(saved.getId());
                                item.setBoleto(boleto);
                            }
                            item.setType(false);
                            item.setMessage("Boleto alterado com sucesso!");
                        }
                        response.add(item);
                    } catch (Exception e) {
                        log.error("Erro ao salvar boleto - Número Título Cliente: {} [Método: alterar]", isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
                        log.error(e.getMessage(), e);
                        throw new FailedAuthenticationException("Erro ao salvar boleto");
                    }
                }
            }
        }
        return response;
    }

    public BoletoDto findByNumeroTituloCliente(String nossonum) {
        Boleto boleto = boletoRepository.findBynumeroTituloCliente(nossonum);

        if (boleto != null) {
            BoletoDto boletoDto = new BoletoDto(boleto);
            return boletoDto;
        }
        return null;
    }

    @Transactional
    public ResponseEntity<BoletoResponseDto> envioBoletoApibanco(BoletoDto boletoDto) {
        Map<String, Object> body = new HashMap<>();
        String url = getDefaultQueryParameters("", "");

        Conta conta = contaRepository.findById(boletoDto.getIdapibanco()).get();
        LoginResponseDto loginResponseDto = authentication(conta);

        if (loginResponseDto.getAccess_token() != null) {
            try {
                Map<String, Object> desconto = new HashMap<>();
                Map<String, Object> segundoDesconto = new HashMap<>();
                Map<String, Object> terceiroDesconto = new HashMap<>();

                Map<String, Object> jurosMora = new HashMap<>();
                Map<String, Object> multa = new HashMap<>();
                Map<String, Object> pagador = new HashMap<>();
                Map<String, Object> beneficiarioFinal = new HashMap<>();

                if (boletoDto != null && boletoDto.getDesconto() != null) {
                    desconto.put("tipo", boletoDto.getDesconto().getTipo());
                    desconto.put("dataExpiracao", boletoDto.getDesconto().getDataExpiracao());
                    desconto.put("porcentagem", boletoDto.getDesconto().getPorcentagem());
                    desconto.put("valor", boletoDto.getDesconto().getValor());
                }
                if (boletoDto != null && boletoDto.getSegundoDesconto() != null) {
                    segundoDesconto.put("tipo", boletoDto.getSegundoDesconto().getTipo());
                    segundoDesconto.put("dataExpiracao", boletoDto.getSegundoDesconto().getDataExpiracao());
                    segundoDesconto.put("porcentagem", boletoDto.getSegundoDesconto().getPorcentagem());
                    segundoDesconto.put("valor", boletoDto.getSegundoDesconto().getValor());
                }
                if (boletoDto != null && boletoDto.getTerceiroDesconto() != null) {
                    terceiroDesconto.put("tipo", boletoDto.getTerceiroDesconto().getTipo());
                    terceiroDesconto.put("dataExpiracao", boletoDto.getTerceiroDesconto().getDataExpiracao());
                    terceiroDesconto.put("porcentagem", boletoDto.getTerceiroDesconto().getPorcentagem());
                    terceiroDesconto.put("valor", boletoDto.getTerceiroDesconto().getValor());
                }
                if (boletoDto != null && boletoDto.getJurosMora() != null) {
                    jurosMora.put("tipo", boletoDto.getJurosMora().getTipo());
                    jurosMora.put("porcentagem", boletoDto.getJurosMora().getPorcentagem());
                    jurosMora.put("valor", boletoDto.getJurosMora().getValor());
                }
                if (boletoDto != null && boletoDto.getMulta() != null) {
                    multa.put("tipo", boletoDto.getMulta().getTipo());
                    multa.put("data", boletoDto.getMulta().getData());
                    multa.put("porcentagem", boletoDto.getMulta().getPorcentagem());
                    multa.put("valor", boletoDto.getMulta().getValor());
                }
                if (boletoDto != null && boletoDto.getPagador() != null) {
                    pagador.put("tipoInscricao", boletoDto.getPagador().getTipoInscricao());
                    pagador.put("numeroInscricao", boletoDto.getPagador().getNumeroInscricao());
                    pagador.put("nome", boletoDto.getPagador().getNome());
                    pagador.put("endereco", boletoDto.getPagador().getEndereco());
                    pagador.put("cep", boletoDto.getPagador().getCep());
                    pagador.put("cidade", boletoDto.getPagador().getCidade());
                    pagador.put("bairro", boletoDto.getPagador().getBairro());
                    pagador.put("uf", boletoDto.getPagador().getUf());
                    if (boletoDto.getPagador().getTelefone() != null) {
                        pagador.put("telefone", boletoDto.getPagador().getTelefone().replaceAll("[^0-9.]", ""));
                    }
                }
                if (boletoDto != null && boletoDto.getBeneficiarioFinal() != null) {
                    beneficiarioFinal.put("tipoInscricao", boletoDto.getBeneficiarioFinal().getTipoInscricao());
                    beneficiarioFinal.put("numeroInscricao", boletoDto.getBeneficiarioFinal().getNumeroInscricao());
                    beneficiarioFinal.put("nome", boletoDto.getBeneficiarioFinal().getNome());
                }

                body.put("numeroConvenio", boletoDto.getNumeroConvenio());
                body.put("numeroCarteira", boletoDto.getNumeroCarteira());
                body.put("numeroVariacaoCarteira", boletoDto.getNumeroVariacaoCarteira());
                body.put("codigoModalidade", boletoDto.getCodigoModalidade());
                body.put("dataEmissao", DateUtil.getDataAtual().isBefore(DateUtil.convertStringToTimestamp(boletoDto.getDataEmissao()).toLocalDateTime()) ? DateUtil.convertLocalDateTimeToString(DateUtil.getDataAtual(), null) : boletoDto.getDataEmissao());
                body.put("dataVencimento", boletoDto.getDataVencimento());
                body.put("valorOriginal", boletoDto.getValorOriginal());
                body.put("valorAbatimento", boletoDto.getValorAbatimento());
                body.put("quantidadeDiasProtesto", boletoDto.getQuantidadeDiasProtesto());
                body.put("quantidadeDiasNegativacao", boletoDto.getQuantidadeDiasNegativacao());
                body.put("orgaoNegativador", boletoDto.getOrgaoNegativador());
                body.put("indicadorAceiteTituloVencido", boletoDto.getIndicadorAceiteTituloVencido());
                body.put("numeroDiasLimiteRecebimento", boletoDto.getNumeroDiasLimiteRecebimento());
                body.put("codigoAceite", boletoDto.getCodigoAceite());
                body.put("codigoTipoTitulo", boletoDto.getCodigoTipoTitulo());
                body.put("descricaoTipoTitulo", boletoDto.getDescricaoTipoTitulo());
                body.put("indicadorPermissaoRecebimentoParcial", boletoDto.getIndicadorPermissaoRecebimentoParcial());
                body.put("numeroTituloBeneficiario",
                        isNull(boletoDto.getNroDocumento()) || boletoDto.getNroDocumento().isEmpty() ?
                                boletoDto.getNumeroTituloBeneficiario() :
                                boletoDto.getNroDocumento()
                );
                body.put("campoUtilizacaoBeneficiario", boletoDto.getCampoUtilizacaoBeneficiario());
                body.put("numeroTituloCliente", boletoDto.getNumeroTituloCliente());
                body.put("mensagemBloquetoOcorrencia", boletoDto.getMensagemBloquetoOcorrencia());
                body.put("desconto", desconto);
                body.put("segundoDesconto", segundoDesconto);
                body.put("terceiroDesconto", terceiroDesconto);
                body.put("jurosMora", jurosMora);
                body.put("multa", multa);
                body.put("pagador", pagador);
                body.put("indicadorPix", boletoDto.getIndicadorPix());

                HttpEntity<?> request = new HttpEntity<>(body, createJSONHeader(loginResponseDto));

                ResponseEntity<RegistroBoletoResponseDto> registroResponse = new RestTemplate().exchange(url, HttpMethod.POST, request, RegistroBoletoResponseDto.class);

                return new ResponseEntity<>(new BoletoResponseDto(registroResponse), registroResponse.getStatusCode());

            } catch (RuntimeException e) {
                ResponseEntity<BoletoResponseDto> response = GetRetornoErro.setRetornoErroBancoBoleto(boletoDto, e);

                boletoDto = GetRetornoErro.verificaQuantidadeTentativas(boletoDto, e);
                if (boletoDto != null) {
                    ArrayList<BoletoDto> boletosDtoList = new ArrayList<BoletoDto>();
                    boletosDtoList.add(boletoDto);
                    alteraBoletosRetornoBanco(boletosDtoList);
                }
                LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e,
                        TipoEventoEnum.EMISSAO);
                if (logEnvioDto != null) {
                    logEnvioService.salvar(logEnvioDto);
                }
                log.error("Erro ao registrar boleto no banco. - Número Título Cliente: {} [Método: envioBoletoApibanco]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
                log.error(e.getMessage(), e);
                return response;
            }
        }
        return null;
    }

    public BoletoResponseDto pesquisar(Integer id) {
        BoletoDto boletoDto = new BoletoDto();
        BoletoResponseDto boletoResponseDto = new BoletoResponseDto();

        Optional<Boleto> boletoOpt = boletoRepository.findById(id);

        if (boletoOpt.isPresent()) {
            boletoDto.setId(boletoOpt.get().getId());
            boletoDto.setIdapibanco(boletoOpt.get().getConta().getId());
            boletoDto.setNossonumero(boletoOpt.get().getNossonumero());
            boletoDto.setNumeroConvenio(boletoOpt.get().getNumeroConvenio());
            boletoDto.setNumeroCarteira(boletoOpt.get().getNumeroCarteira());
            boletoDto.setNumeroVariacaoCarteira(boletoOpt.get().getNumeroVariacaoCarteira());
            boletoDto.setCodigoModalidade(boletoOpt.get().getCodigoModalidade());
            boletoDto.setDataEmissao(boletoOpt.get().getDataEmissao().toString());
            boletoDto.setDataVencimento(boletoOpt.get().getDataVencimento().toString());
            boletoDto.setValorOriginal(boletoOpt.get().getValorOriginal());
            boletoDto.setValorAbatimento(boletoOpt.get().getValorAbatimento());
            boletoDto.setQuantidadeDiasProtesto(boletoOpt.get().getQuantidadeDiasProtesto());
            boletoDto.setQuantidadeDiasNegativacao(boletoOpt.get().getQuantidadeDiasNegativacao());
            boletoDto.setOrgaoNegativador(boletoOpt.get().getOrgaoNegativador());
            boletoDto.setIndicadorAceiteTituloVencido(boletoOpt.get().getIndicadorAceiteTituloVencido());
            boletoDto.setNumeroDiasLimiteRecebimento(boletoOpt.get().getNumeroDiasLimiteRecebimento());
            boletoDto.setCodigoAceite(boletoOpt.get().getCodigoAceite());
            boletoDto.setCodigoTipoTitulo(boletoOpt.get().getCodigoTipoTitulo());
            boletoDto.setDescricaoTipoTitulo(boletoOpt.get().getDescricaoTipoTitulo());
            boletoDto.setIndicadorPermissaoRecebimentoParcial(boletoOpt.get().getIndicadorPermissaoRecebimentoParcial());
            boletoDto.setNumeroTituloBeneficiario(boletoOpt.get().getNumeroTituloBeneficiario());
            boletoDto.setCampoUtilizacaoBeneficiario(boletoOpt.get().getCampoUtilizacaoBeneficiario());
            boletoDto.setNumeroTituloCliente(boletoOpt.get().getNumeroTituloCliente());
            boletoDto.setMensagemBloquetoOcorrencia(boletoOpt.get().getMensagemBloquetoOcorrencia());
            boletoDto.setIndicadorPix(boletoOpt.get().getIndicadorPix());
            boletoDto.setStatus(boletoOpt.get().getStatus().getStatus());

            boletoResponseDto.setBoleto(boletoDto);

        } else {
            log.info("Boleto não existe na base de dados - Id: {} [Método: pesquisar]", isNull(id) ? "Nulo" : id.toString());
            throw new NotFoundException("Boleto não existe na base de dados");
        }

        return boletoResponseDto;
    }

    public void atualizaStatusBoletos(Conta conta) {
        int[] statusFinais = {StatusBoletoEnum.LIQUIDADO.getStatus(), StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus(),
                StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus(),
                StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus(),
                StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus(),
                StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus(), StatusBoletoEnum.TITULO_CREDITADO.getStatus(),
                StatusBoletoEnum.ERRO.getStatus(), StatusBoletoEnum.ALTERA_BOLETO.getStatus()};
        List<Integer> list = Arrays.stream(statusFinais).boxed().collect(Collectors.toList());
        List<Boleto> boletos = getStatusBoletoNotInStatus(list,conta.getId());
        for (Boleto boleto : boletos) {
            if (boleto.getConta().getId() != null) {
                    try {
                        ResponseEntity<BoletoRetornoDto> response = detalharBoleto(boleto);
                        BoletoRetornoDto boletoApi = response.getBody();
                        boletoApi.setIdboleto(boleto.getId());
                        if (boletoApi != null && boletoApi.getCodigoEstadoTituloCobranca() != null) {
                            insereRetornoBancoLogEnvio(response, boleto, boletoApi);
                            updateStatusBoleto(boleto, boletoApi);
                        }
                    } catch (Exception e) {
                        log.error("Erro ao atualizar status do boleto. - Número Título Cliente: {} [Método: atualizaStatusBoletos]", isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
                        log.error(e.getMessage(), e);
                        BoletoDto boletoDto = new BoletoDto(boleto);
                        if (!StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus().equals(boletoDto.getStatus())) {
                            LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e,
                                    TipoEventoEnum.RETORNO_BANCO);
                            if (logEnvioDto != null) {
                                logEnvioService.salvar(logEnvioDto);
                            }
                        }
                    } 
                }
        }
    }

    public List<Boleto> getStatusBoleto(int[] statusFinais) {
        return boletoRepository.findDistinctByStatusNotIn(statusFinais);
    }
    
    public List<Boleto> getStatusBoletoNotInStatus(List<Integer> statusFinais, Integer idConta) {
    	if(idConta == null || idConta == 0) {
    		return boletoRepository.findDistinctByStatusNotIn(statusFinais);
    	} else {
    		return boletoRepository.findDistinctByStatusNotIn(statusFinais,idConta);
    	}
    }

    public void insereRetornoBancoLogEnvio(ResponseEntity<BoletoRetornoDto> response, Boleto boleto, BoletoRetornoDto boletoApi) {
        if (!boleto.getStatus().getStatus().equals(boletoApi.getCodigoEstadoTituloCobranca())
                && !StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus().equals(boleto.getStatus().getStatus())) {
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                StringBuilder stacktrace = new StringBuilder().append("Status ").append(response.getStatusCodeValue())
                        .append(": ").append("Executado com sucesso");
                LogEnvioDto logEnvioDto = new LogEnvioDto(boleto.getNumeroTituloCliente(),
                        TipoEventoEnum.RETORNO_BANCO.getDescricaoEvento(), stacktrace.toString(),
                        boletoApi.getCodigoEstadoTituloCobranca(), TipoEventoEnum.RETORNO_BANCO.getIdTipoEvento(),
                        SituacaoEnum.PROCESSADA.getIdSituacao(), boletoApi.getCodigoEstadoTituloCobranca());
                logEnvioService.salvar(logEnvioDto);
            } else {
                BoletoDto boletoDto = new BoletoDto(boleto);
                LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, response,
                        TipoEventoEnum.RETORNO_BANCO);
                if (logEnvioDto != null) {
                    logEnvioService.salvar(logEnvioDto);
                }
            }
        }
    }

    @Transactional
    public void updateStatusBoleto(final Boleto boleto, final BoletoRetornoDto boletoApi) {
        boleto.setStatus(boletoApi.getCodigoEstadoTituloCobranca());
        boleto.setStatusBanco(boletoApi.getCodigoEstadoTituloCobranca());
        boleto.setDhCreditoLiquidacao(boletoApi.getDataCreditoLiquidacao());
        boleto.setDhRecebimentoTitulo(boletoApi.getDataRecebimentoTitulo());
        boleto.setValorMultaRecebido(boletoApi.getValorMultaRecebido());
        boleto.setValorJuroMoraRecebido(boletoApi.getValorJuroMoraRecebido());
        if(!isStatusFinal(boletoApi.getCodigoEstadoTituloCobranca(), BancoEnum.BANCO_DO_BRASIL.getId() )) {
            boletoRepository.save(boleto);
        } else {
            salvarBoletoStatusFinal(boleto);
        }
    }
    
    @Transactional
    public void salvarBoletoStatusFinal(Boleto boleto) {
        BoletoStatusFinal boletoStatusFinal = new BoletoStatusFinal(boleto);
        if (boletoStatusFinal.getAudit() != null) {
        	boletoStatusFinal.getAudit().setDataUpdated(DateUtil.getDataAtual());
        } else {
			Audit audit = new Audit(DateUtil.getDataAtual(), DateUtil.getDataAtual());
			boletoStatusFinal.setAudit(audit);
		}
        boletoStatusFinalRepository.save(boletoStatusFinal);
    }

    @Transactional
    public void deletarBoleto(Integer idBoleto) {
        boolean existeBoleto = boletoStatusFinalRepository.existsById(idBoleto);
        if(existeBoleto) {
            boletoRepository.deleteById(idBoleto);
        }
    }

    @Transactional
    public void salvaRetornoBoleto(BoletoRetornoDto boletoRetornoDto, Optional<BoletoRetorno> boletoRetornoUpdate) {
        if (boletoRetornoDto != null) {
            if (boletoRetornoUpdate.isPresent()) {
                boletoRetornoDto.setId(boletoRetornoUpdate.get().getId());
            }
            BoletoRetorno boletoRetorno = new BoletoRetorno(boletoRetornoDto);
            boletoRetornoRepository.save(boletoRetorno);
        }
    }
    
    public ResponseEntity<BoletoRetornoDto> detalharBoletoQueue(final Boleto boleto, LoginResponseDto loginResponseDto)  throws Exception {

            ResponseEntity<BoletoRetornoDto> response = new ResponseEntity<>(HttpStatus.OK);

            if (loginResponseDto.getAccess_token() != null) {
                HttpEntity<?> httpEntity = new HttpEntity<>(createJSONHeader(loginResponseDto));

                String params = "/" + boleto.getNumeroTituloCliente();

                String query = "&numeroConvenio=" + boleto.getNumeroConvenio();

                response = new RestTemplate().exchange(getDefaultQueryParameters(params, query), HttpMethod.GET,
                        httpEntity, BoletoRetornoDto.class);
            }

            return response;

    }

    public ResponseEntity<BoletoRetornoDto> detalharBoleto(final Boleto boleto) {
        Conta conta = contaRepository.findById(boleto.getConta().getId()).get();
        LoginResponseDto loginResponseDto = authentication(conta);

        try {
            ResponseEntity<BoletoRetornoDto> response = new ResponseEntity<>(HttpStatus.OK);

            if (loginResponseDto.getAccess_token() != null) {
                HttpEntity<?> httpEntity = new HttpEntity<>(createJSONHeader(loginResponseDto));

                String params = "/" + boleto.getNumeroTituloCliente();

                String query = "&numeroConvenio=" + boleto.getNumeroConvenio();

                response = new RestTemplate().exchange(getDefaultQueryParameters(params, query), HttpMethod.GET,
                        httpEntity, BoletoRetornoDto.class);
            }

            return response;

        } catch (RuntimeException e) {
            log.error("Erro ao obter detalhes do boleto junto ao banco. - Número Título Cliente: {} [Método: detalharBoleto]", isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
            log.error(e.getMessage(), e);
            throw new FailedAuthenticationException("O teste das credenciais com o banco falhou");
        }
    }

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

    private String getDefaultQueryParameters(String params, String query) {
        return urlApiBbCobranca + params + gwDevAppKey + query;
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

    public ResponseEntity<CancelamentoBoletoResponseDto> cancelaBoletoApibanco(BoletoDto boletoDto) {
        String params = "/" + boletoDto.getNumeroTituloCliente() + "/baixar";

        String url = getDefaultQueryParameters(params, "");

        Conta conta = contaRepository.findById(boletoDto.getIdapibanco()).get();
        LoginResponseDto loginResponseDto = authentication(conta);

        if (loginResponseDto.getAccess_token() != null) {
            try {

                Map<String, Object> body = new HashMap<>();
                body.put("numeroConvenio", boletoDto.getNumeroConvenio());

                HttpEntity<?> request = new HttpEntity<>(body, createJSONHeader(loginResponseDto));

                return new RestTemplate().exchange(url, HttpMethod.POST, request, CancelamentoBoletoResponseDto.class);

            } catch (RuntimeException e) {
                boletoDto = GetRetornoErro.verificaQuantidadeTentativas(boletoDto, e);
                LogEnvioDto logEnvioDto = logEnvioService.findByNossonumeroAndStacktrace(boletoDto.getNumeroTituloCliente(), "Status 201: Criado com sucesso");
                boletoDto = GetRetornoErro.verificaResponseNotFound(boletoDto, e, logEnvioDto);
                if (boletoDto != null) {
                    ArrayList<BoletoDto> boletosDtoList = new ArrayList<BoletoDto>();
                    boletosDtoList.add(boletoDto);
                    alteraBoletosRetornoBanco(boletosDtoList);
                }
                logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e,
                        TipoEventoEnum.BAIXA_CANCELAMENTO);
                if (logEnvioDto != null) {
                    logEnvioService.salvar(logEnvioDto);
                }
                log.error("Erro ao cancelar boleto no banco. - Número Título Cliente: {} [Método: cancelaBoletoApibanco]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    private BoletoDto alteracaoBoleto(BoletoDto boletoDto) {
        boolean isAlteracao = false;
        BoletoDto boletoAux = findByNumeroTituloCliente(boletoDto.getNumeroTituloCliente());

        boletoDto.setIndicadorNovaDataVencimento("N");
        boletoDto.setIndicadorAtribuirDesconto("N");
        boletoDto.setIndicadorAlterarDesconto("N");
        boletoDto.setIndicadorAlterarDataDesconto("N");
        boletoDto.setIndicadorProtestar("N");
        boletoDto.setIndicadorSustacaoProtesto("N");
        boletoDto.setIndicadorCancelarProtesto("N");
        boletoDto.setIndicadorIncluirAbatimento("N");
        boletoDto.setIndicadorCancelarAbatimento("N");
        boletoDto.setIndicadorCobrarJuros("N");
        boletoDto.setIndicadorDispensarJuros("N");
        boletoDto.setIndicadorCobrarMulta("N");
        boletoDto.setIndicadorDispensarMulta("N");
        boletoDto.setIndicadorNegativar("N");
        boletoDto.setIndicadorAlterarNossoNumero("N");
        boletoDto.setIndicadorAlterarEnderecoPagador("N");
        boletoDto.setIndicadorAlterarPrazoBoletoVencido("N");
        boletoDto.setIndicadorAlterarValorOriginal("N");

        boletoDto.setCodigoLinhaDigitavel(boletoAux.getCodigoLinhaDigitavel());
        boletoDto.setDataCreditoLiquidacao(boletoAux.getDataCreditoLiquidacao());
        boletoDto.setDataRecebimentoTitulo(boletoAux.getDataRecebimentoTitulo());
        boletoDto.setValorJuroMoraRecebido(boletoAux.getValorJuroMoraRecebido());
        boletoDto.setValorMultaRecebido(boletoAux.getValorMultaRecebido());
        boletoDto.setDataRegistroBanco(boletoAux.getDataRegistroBanco());
        
        boletoDto.setMulta(boletoAux.getMulta());
        boletoDto.setJurosMora(boletoAux.getJurosMora());

        if (!isStatusFinal(boletoAux.getStatus()) || StatusBoletoEnum.ALTERA_BOLETO.getStatus().equals(boletoAux.getStatus())) {
            if ("S".equals(boletoAux.getIndicadorNovaDataVencimento()) || !boletoAux.getDataVencimento().equals(boletoDto.getDataVencimento())) {
                boletoDto.setIndicadorNovaDataVencimento("S");
                isAlteracao = true;
            }

            if ("S".equals(boletoAux.getIndicadorAtribuirDesconto()) || (boletoAux.getDesconto() == null && boletoDto.getDesconto() != null)) {
                boletoDto.setIndicadorAtribuirDesconto("S");
                isAlteracao = true;
            } 
            
            if ("S".equals(boletoAux.getIndicadorAlterarDesconto()) || 
            		((!"S".equals(boletoAux.getIndicadorAtribuirDesconto()) && !"S".equals(boletoDto.getIndicadorAtribuirDesconto())) && 
            				boletoDto.getDesconto() != null && !boletoDto.getDesconto().equals(boletoAux.getDesconto()))) {
            		boletoDto.setIndicadorAlterarDesconto("S");
            		isAlteracao = true;
            		if (!boletoDto.getDesconto().getDataExpiracao().isBlank() && !boletoAux.getDesconto().getDataExpiracao().equals(boletoDto.getDesconto().getDataExpiracao())) {
            			boletoDto.setIndicadorAlterarDataDesconto("S");
            			isAlteracao = true;
            		}
            }
            
        	if ("S".equals(boletoAux.getIndicadorAlterarDesconto()) || 
            		((!"S".equals(boletoAux.getIndicadorAtribuirDesconto()) && !"S".equals(boletoDto.getIndicadorAtribuirDesconto())) && 
            				boletoAux.getDesconto() != null && (boletoDto.getDesconto() == null ||
                    ((boletoDto.getDesconto().getValor() != null && boletoDto.getDesconto().getValor() == 0.0) &&
                            (boletoDto.getDesconto().getPorcentagem() != null && boletoDto.getDesconto().getPorcentagem() == 0.0))))) {
                boletoDto.setIndicadorAlterarDesconto("S");
                isAlteracao = true;
            }

            if (boletoAux.getSegundoDesconto() == null && boletoDto.getSegundoDesconto() != null) {
                boletoDto.setIndicadorAtribuirDesconto("S");
                isAlteracao = true;
            } else if (boletoDto.getSegundoDesconto() != null
                    && !boletoDto.getSegundoDesconto().equals(boletoAux.getSegundoDesconto())) {
                boletoDto.setIndicadorAlterarDesconto("S");
                isAlteracao = true;
                if (!boletoDto.getSegundoDesconto().getDataExpiracao().isBlank() && !boletoAux.getSegundoDesconto().getDataExpiracao().equals(boletoDto.getSegundoDesconto().getDataExpiracao())) {
                    boletoDto.setIndicadorAlterarDataDesconto("S");
                    isAlteracao = true;
                }
            } else if (boletoAux.getSegundoDesconto() != null && (boletoDto.getSegundoDesconto() == null ||
                    ((boletoDto.getSegundoDesconto().getValor() != null && boletoDto.getSegundoDesconto().getValor() == 0.0) &&
                            (boletoDto.getSegundoDesconto().getPorcentagem() != null && boletoDto.getSegundoDesconto().getPorcentagem() == 0.0)))) {
                boletoDto.setIndicadorAlterarDesconto("S");
                isAlteracao = true;
            }

            if (boletoAux.getTerceiroDesconto() == null && boletoDto.getTerceiroDesconto() != null) {
                boletoDto.setIndicadorAtribuirDesconto("S");
                isAlteracao = true;
            } else if (boletoDto.getTerceiroDesconto() != null
                    && !boletoDto.getTerceiroDesconto().equals(boletoAux.getTerceiroDesconto())) {
                boletoDto.setIndicadorAlterarDesconto("S");
                isAlteracao = true;
                if (!boletoDto.getTerceiroDesconto().getDataExpiracao().isBlank() && !boletoAux.getTerceiroDesconto().getDataExpiracao().equals(boletoDto.getTerceiroDesconto().getDataExpiracao())) {
                    boletoDto.setIndicadorAlterarDataDesconto("S");
                    isAlteracao = true;
                }
            } else if (boletoAux.getTerceiroDesconto() != null && (boletoDto.getTerceiroDesconto() == null ||
                    ((boletoDto.getTerceiroDesconto().getValor() != null && boletoDto.getTerceiroDesconto().getValor() == 0.0) &&
                            (boletoDto.getTerceiroDesconto().getPorcentagem() != null && boletoDto.getTerceiroDesconto().getPorcentagem() == 0.0)))) {
                boletoDto.setIndicadorAlterarDesconto("S");
                isAlteracao = true;
            }

            if ("S".equals(boletoAux.getIndicadorProtestar()) ||  
            		(boletoDto.getQuantidadeDiasProtesto() != null && boletoDto.getQuantidadeDiasProtesto() > 0
                    && !boletoDto.getQuantidadeDiasProtesto().equals(boletoAux.getQuantidadeDiasProtesto()))) {
                boletoDto.setIndicadorProtestar("S");
                isAlteracao = true;
            }

            if ("S".equals(boletoAux.getIndicadorIncluirAbatimento()) || 
            		(boletoDto.getValorAbatimento() != null && boletoDto.getValorAbatimento() > 0
                    && !boletoDto.getValorAbatimento().equals(boletoAux.getValorAbatimento()))) {
                boletoDto.setIndicadorIncluirAbatimento("S");
                isAlteracao = true;
            }

            if ("S".equals(boletoAux.getIndicadorNegativar()) || 
            		(boletoDto.getTipoNegativacao() != null && boletoAux.getQuantidadeDiasNegativacao() != null
                    && boletoDto.getQuantidadeDiasNegativacao() != null)) {
                if (boletoDto.getTipoNegativacao() > 0 && boletoDto.getTipoNegativacao() <= 4
                        && !boletoDto.getQuantidadeDiasNegativacao().equals(boletoAux.getQuantidadeDiasNegativacao())) {
                    boletoDto.setIndicadorNegativar("S");
                    isAlteracao = true;
                }
            }

            if ("S".equals(boletoAux.getIndicadorAlterarEnderecoPagador()) || 
            		(boletoDto.getPagador() != null && !boletoDto.getPagador().equals(boletoAux.getPagador()))) {
                boletoDto.setIndicadorAlterarEnderecoPagador("S");
                isAlteracao = true;
            }

            if ("S".equals(boletoAux.getIndicadorAlterarPrazoBoletoVencido()) ||
            		(boletoDto.getQuantidadeDiasAceite() != null && boletoDto.getQuantidadeDiasAceite() > 0
                    && !boletoDto.getQuantidadeDiasAceite().equals(boletoAux.getQuantidadeDiasAceite()))) {
                boletoDto.setIndicadorAlterarPrazoBoletoVencido("S");
                isAlteracao = true;
            }
            
            if ("S".equals(boletoAux.getIndicadorAlterarValorOriginal()) ||
            		(boletoDto.getValorOriginal() != null && !boletoDto.getValorOriginal().equals(boletoAux.getValorOriginal()))) {
				boletoDto.setIndicadorAlterarValorOriginal("S");
				isAlteracao = true;
			}

            if (isAlteracao) {
                boletoDto.setStatus(StatusBoletoEnum.ALTERA_BOLETO.getStatus());
            }

            boletoDto.setStatusBanco(verificaStatusBanco(boletoDto.getStatus()));
        }

        return boletoDto;
    }

    public void atualizaBoletos(String dataInicial, String dataFinal, Integer idConta, Integer agencia, String status) {
        Conta conta = contaRepository.findById(idConta).get();

        LoginResponseDto loginResponseDto = authentication(conta);
        Optional<Conta> contaOpt = contaRepository.findById(idConta);

        try {
            if (loginResponseDto.getAccess_token() != null) {

                HttpEntity<?> httpEntity = new HttpEntity<>(createJSONHeader(loginResponseDto));

                String query = "&indicadorSituacao=" + status + "&agenciaBeneficiario=" + agencia
                        + "&contaBeneficiario=" + conta + "&dataInicioRegistro=" + dataInicial + "&dataFimRegistro="
                        + dataFinal;

                ResponseEntity<ListagemBoletosResponseDto> response = new RestTemplate().exchange(
                        getDefaultQueryParameters("", query), HttpMethod.GET, httpEntity,
                        ListagemBoletosResponseDto.class);

                if (response.getStatusCode().equals(HttpStatus.OK) && !response.getBody().getBoletos().isEmpty()) {

                    ArrayList<ListagemBoletoResponseDto> boletosResponseDto = response.getBody().getBoletos();
                    ArrayList<BoletoDto> boletoDtos = new ArrayList<>();

                    if (contaOpt.isPresent()) {
                        for (ListagemBoletoResponseDto boletoResponseDto : boletosResponseDto) {
                            Boleto boletoOpt = boletoRepository
                                    .findBynumeroTituloCliente(boletoResponseDto.getNumeroBoletoBB());
                            if (boletoOpt == null && contaOpt.get().getConvenio() != null) {
                                BoletoDto boletoDto = new BoletoDto(boletoResponseDto);
                                boletoDto.setNumeroConvenio(contaOpt.get().getConvenio());
                                boletoDto.setIdapibanco(idConta);
                                boletoDtos.add(boletoDto);
                            }
                        }
                    }

                    try {
                        if (!boletoDtos.isEmpty()) {
                            processaBoletos(boletoDtos);
                            boletoDtos.clear();
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw new FailedAuthenticationException("Erro ao salvar boleto");
                    }

                    while ("S".equals(response.getBody().getIndicadorContinuidade())) {
                        loginResponseDto = authentication(conta);

                        if (loginResponseDto.getAccess_token() != null) {

                            httpEntity = new HttpEntity<>(createJSONHeader(loginResponseDto));

                            String queryContinuidade = "&indicadorSituacao=" + status + "&agenciaBeneficiario="
                                    + agencia + "&contaBeneficiario=" + conta + "&dataInicioRegistro=" + dataInicial
                                    + "&dataFimRegistro=" + dataFinal + "&indice="
                                    + response.getBody().getProximoIndice();

                            response = new RestTemplate().exchange(getDefaultQueryParameters("", queryContinuidade),
                                    HttpMethod.GET, httpEntity, ListagemBoletosResponseDto.class);

                            boletosResponseDto = response.getBody().getBoletos();

                            if (response.getStatusCode().equals(HttpStatus.OK)) {
                                for (ListagemBoletoResponseDto boletoResponseDto : boletosResponseDto) {

                                    Boleto boletoOpt = boletoRepository
                                            .findBynumeroTituloCliente(boletoResponseDto.getNumeroBoletoBB());

                                    if (boletoOpt == null && contaOpt.get().getConvenio() != null) {
                                        BoletoDto boletoDto = new BoletoDto(boletoResponseDto);
                                        boletoDto.setNumeroConvenio(contaOpt.get().getConvenio());
                                        boletoDto.setIdapibanco(idConta);
                                        boletoDtos.add(boletoDto);
                                    }
                                }

                                try {
                                    if (!boletoDtos.isEmpty()) {
                                        processaBoletos(boletoDtos);
                                        boletoDtos.clear();
                                    }
                                } catch (Exception e) {
                                    log.error(e.getMessage(), e);
                                    throw new FailedAuthenticationException("Erro ao salvar boleto");
                                }
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            throw new FailedAuthenticationException("O teste das credenciais com o banco falhou");
        }
    }
    
	public void atualizaBoletosBaixadosDiariamente(Job job) {

		Integer count = 0;
		try {
			List<Integer> statusFinais = new ArrayList<Integer>();
			statusFinais.add(StatusBoletoEnum.LIQUIDADO.getStatus());
			statusFinais.add(StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());
			statusFinais.add(StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus());
			statusFinais.add(StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus());
			statusFinais.add(StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus());
			statusFinais.add(StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus());
			statusFinais.add(StatusBoletoEnum.TITULO_CREDITADO.getStatus());
			statusFinais.add(StatusBoletoEnum.ERRO.getStatus());
			statusFinais.add(StatusBoletoEnum.ALTERA_BOLETO.getStatus());
			
			List<Conta> contas = contaRepository.buscarContasQueTemBoletos(statusFinais);
			for (Conta conta : contas) {
				List<Boleto> boletos = boletoRepository.findByBoletosPendentes(statusFinais, conta.getId());
				Map<String, Boleto> boletoMap = new HashMap<String, Boleto>();
				count = count + boletos.size();
				for (Boleto boleto : boletos) {
					boletoMap.put(boleto.getNumeroTituloCliente(), boleto);
				}
				try {
					Timestamp dtInicial = Timestamp.valueOf(LocalDateTime.now().minusMinutes(1440));
					job.setDataProxExec(dtInicial);
					envioStatus(authentication(conta), "B", conta, DateUtil.convertTimeStampToString(dtInicial),
							boletoMap,true);
				} catch (HttpServerErrorException e) {
					log.error(e.getMessage(), e);
					if (HttpStatus.INTERNAL_SERVER_ERROR.equals(e.getStatusCode())) {
						System.out.println("***** CONTA ID: " + conta.getId() + " AGENCIA : " + conta.getCodage()
								+ " CONTA : " + conta.getCodctabco() + " ERROR" + e.getMessage());
					}
				} catch (Exception ee) {
					log.error(ee.getMessage(), ee);
				}
			}
		} catch (Exception e) {
			job.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if (count > 0 || job.getError() != null) {
				job.setQtdRegistro(count);
				job.setDataFinal(Timestamp.valueOf(LocalDateTime.now()));
				Timestamp proxEx = Timestamp.valueOf(LocalDateTime.now().plusMinutes(JobEnum.valueByTempo(job.getTipo())));
				job.setDataProxExec(proxEx);
				job.setStatus(1);
				jobRepository.save(job);
			} else {
				jobRepository.deleteById(job.getId());
			}
		}
	} 
    
	public void atualizaBoletosBaixados(Job job) {

		Integer count = 0;
		String acao = "";
		try {
			List<String> acoes = new ArrayList<>();
			acoes = acoesRepository.findDistinctByAcoes();
			for (String acoes2 : acoes) {
				acao = acao +  acoes2.substring(0, acoes2.length() - 1) + ",";
			}
			acao = acao.substring(0, acao.length() - 1);
			HttpEntity<?> httpEntity = new HttpEntity<>(createJSONHeaderAcao());

			String params = "" + acao;

			try {
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(getDefaultQueryParameters(params),
						HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);
				
				for (AcaoRetornoDto4 string : response.getBody().getResults()) {
					if(string.getRegularMarketPrice() != null) {
						acoesRepository.alteraPriceCurrency(string.getRegularMarketPrice(), LocalDateTime.now(), string.getShortName(), string.getSymbol());
						List<Acoes> acoess = acoesRepository.findDistinctByAcoes(string.getSymbol());
						for (Acoes acoesss : acoess) {
							if("C".equals(acoesss.getTipo())) {
								acoesss.setLucropreju(string.getRegularMarketPrice() - acoesss.getValorsuj() );
							} else {
								acoesss.setLucropreju(acoesss.getValorsuj() - string.getRegularMarketPrice());
							}
							acoesRepository.save(acoesss);
						}
					}
						
				}

				System.out.println("teste");
			} catch (HttpClientErrorException ee) {
				httpEntity = new HttpEntity<>(createJSONHeaderAcao2());
				params = "" + acao;
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(getDefaultQueryParameters(params),
						HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);

				System.out.println("teste");
			} catch (IllegalArgumentException eed) {
				httpEntity = new HttpEntity<>(createJSONHeaderAcao3());
				params = "" + acao;
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(getDefaultQueryParameters(params),
						HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);

				System.out.println("teste");
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
    
	private Integer envioStatus(LoginResponseDto loginResponseDto, String status, Conta conta, String dataInicial,
			Map<String, Boleto> mapBoletos, boolean isMovimento) throws Exception {
        Integer count = 0;
        if (loginResponseDto.getAccess_token() != null) {
            if (conta != null && conta.getCodctabco() != null && conta.getCodage() != null) {
                HttpEntity<?> httpEntity = new HttpEntity<>(createJSONHeader(loginResponseDto));
                 //TODO rever o fluxo do digito da conta ( remover o ultimo digito da conta do MS)
                String codContaSemDig = conta.getCodctabco().toString();
                String codAgencSemDig = conta.getCodage().toString();
                if(codContaSemDig.length() > 6) {
                	int tamanho = codContaSemDig.length() - 6;
                	codContaSemDig = codContaSemDig.substring(0, codContaSemDig.length() - tamanho);
                }
                if(codAgencSemDig.length() > 4) {
                	int tamanho = codAgencSemDig.length() - 4;
                	codAgencSemDig = codAgencSemDig.substring(0, codAgencSemDig.length() - tamanho);
                }
                String query = "";
                if(isMovimento) {
                    query = "&indicadorSituacao=" + status + "&agenciaBeneficiario=" + conta.getCodage()
                            + "&contaBeneficiario=" + codContaSemDig + "&dataInicioMovimento=" + dataInicial
                            + "&dataFimMovimento=" + DateUtil.convertLocalDateTimeToString(DateUtil.getDataAtual());
                } else {
                    query = "&indicadorSituacao=" + status + "&agenciaBeneficiario=" + conta.getCodage()
                            + "&contaBeneficiario=" + conta.getCodctabco() + "&dataInicioRegistro=" + dataInicial
                            + "&dataFimRegistro=" + DateUtil.convertLocalDateTimeToString(DateUtil.getDataAtual());
                }

                ResponseEntity<ListagemBoletosResponseDto> response = new RestTemplate().exchange(
                        getDefaultQueryParameters("", query), HttpMethod.GET, httpEntity,
                        ListagemBoletosResponseDto.class);

                if (response.getStatusCode().equals(HttpStatus.OK) && !response.getBody().getBoletos().isEmpty()) {

                    ArrayList<ListagemBoletoResponseDto> boletosResponseDto = response.getBody().getBoletos();
                    ArrayList<Boleto> boletoDtos = new ArrayList<Boleto>();
                    ArrayList<Boleto> boletoStatusFinais = new ArrayList<Boleto>();
                    ArrayList<LogEnvio> logEnvioDto = new ArrayList<LogEnvio>();
                    List<Integer> statusFinaisBB = statusFinaisBoletoBB();
                    for (ListagemBoletoResponseDto boletoResponseDto : boletosResponseDto) {
                        if (mapBoletos != null &&
                                 mapBoletos.get(boletoResponseDto.getNumeroBoletoBB()) != null &&
                                !mapBoletos.get(boletoResponseDto.getNumeroBoletoBB()).getStatus().getStatus().equals(boletoResponseDto.getCodigoEstadoTituloCobranca())) {
                            BoletoDto boletoDto = new BoletoDto(mapBoletos.get(boletoResponseDto.getNumeroBoletoBB()));
                            boletoDto.setStatus(boletoResponseDto.getCodigoEstadoTituloCobranca());
                            boletoDto.setStatusBanco(boletoResponseDto.getCodigoEstadoTituloCobranca());
                            boletoDto.setValorPago(boletoResponseDto.getValorPago());
                            boletoDto.setValorAtual(boletoResponseDto.getValorAtual());
                            boletoDto.setDataCredito(boletoResponseDto.getDataCredito());
                            boletoDto.setDataCreditoLiquidacao(boletoResponseDto.getDataCredito());
                            boletoDto.setDataRecebimentoTitulo(boletoResponseDto.getDataMovimento());
                            Boleto bol = new Boleto(boletoDto);
                            if(boletoResponseDto.getEstadoTituloCobranca() != null && statusFinaisBB.contains(boletoResponseDto.getCodigoEstadoTituloCobranca())) {
                                boletoStatusFinais.add(bol);
                            } else {
                                boletoDtos.add(bol);
                            }
                            count = count + 1;
                            logEnvioDto.add(new LogEnvio(boletoDto.getNumeroTituloCliente(),TipoEventoEnum.RETORNO_BANCO.getDescricaoEvento(),"Atualizado com sucesso",
                                    boletoResponseDto.getCodigoEstadoTituloCobranca(), TipoEventoEnum.RETORNO_BANCO.getIdTipoEvento(),
                                    SituacaoEnum.PROCESSADA.getIdSituacao(), boletoResponseDto.getCodigoEstadoTituloCobranca()));
                        }
                    }

                    if(!boletoStatusFinais.isEmpty()) {
                        List<BoletoStatusFinal> boletosStatusFinais = boletoToBoletoStatusFinais(boletoStatusFinais);
                        boletoStatusFinalRepository.saveAll(boletosStatusFinais);
                        for (BoletoStatusFinal boletoStatusFinal : boletosStatusFinais) {
                            boolean existeBoleto = boletoStatusFinalRepository.existsById(boletoStatusFinal.getId());
                            if(existeBoleto) {
                                boletoRepository.deleteById(boletoStatusFinal.getId());
                            }
                        }
                        logEnvioRepository.saveAll(logEnvioDto);
                        logEnvioDto.clear();
                    }


                    if (!boletoDtos.isEmpty()) {
                        boletoRepository.saveAll(boletoDtos);
                        boletoDtos.clear();
                        logEnvioRepository.saveAll(logEnvioDto);
                        logEnvioDto.clear();
                    }

                    while ("S".equals(response.getBody().getIndicadorContinuidade())) {

                        if (loginResponseDto.getAccess_token() != null) {
                            httpEntity = new HttpEntity<>(createJSONHeader(loginResponseDto));
                            codContaSemDig = conta.getCodctabco().toString();
                            codAgencSemDig = conta.getCodage().toString();
                            if(codContaSemDig.length() > 6) {
                            	int tamanho = codContaSemDig.length() - 6;
                            	codContaSemDig = codContaSemDig.substring(0, codContaSemDig.length() - tamanho);
                            }
                            if(codAgencSemDig.length() > 4) {
                            	int tamanho = codAgencSemDig.length() - 4;
                            	codAgencSemDig = codAgencSemDig.substring(0, codAgencSemDig.length() - tamanho);
                            }
                            String queryContinuidade = "&indicadorSituacao=" + status + "&agenciaBeneficiario="
                                    + codAgencSemDig + "&contaBeneficiario=" + codContaSemDig
                                    + "&dataInicioRegistro=" + dataInicial + "&dataFimRegistro="
                                    + DateUtil.convertLocalDateTimeToString(DateUtil.getDataAtual()) + "&indice="
                                    + response.getBody().getProximoIndice();

                            response = new RestTemplate().exchange(getDefaultQueryParameters("", queryContinuidade),
                                    HttpMethod.GET, httpEntity, ListagemBoletosResponseDto.class);
                            boletosResponseDto = response.getBody().getBoletos();

                            if (response.getStatusCode().equals(HttpStatus.OK)) {
                                for (ListagemBoletoResponseDto boletoResponseDto : boletosResponseDto) {
                                    if (mapBoletos != null
                                            && mapBoletos.get(boletoResponseDto.getNumeroBoletoBB()) != null &&
                                            !mapBoletos.get(boletoResponseDto.getNumeroBoletoBB()).getStatus().getStatus().equals(boletoResponseDto.getCodigoEstadoTituloCobranca())) {
                                        BoletoDto boletoDto = new BoletoDto(mapBoletos.get(boletoResponseDto.getNumeroBoletoBB()));
                                        boletoDto.setStatus(boletoResponseDto.getCodigoEstadoTituloCobranca());
                                        boletoDto.setStatusBanco(boletoResponseDto.getCodigoEstadoTituloCobranca());
                                        boletoDto.setValorPago(boletoResponseDto.getValorPago());
                                        boletoDto.setValorAtual(boletoResponseDto.getValorAtual());
                                        boletoDto.setDataCredito(boletoResponseDto.getDataCredito());
                                        boletoDto.setDataCreditoLiquidacao(boletoResponseDto.getDataCredito());
                                        boletoDto.setDataRecebimentoTitulo(boletoResponseDto.getDataMovimento());
                                        Boleto boleto = new Boleto(boletoDto);
                                        if(boletoResponseDto.getEstadoTituloCobranca() != null && statusFinaisBB.contains(boletoResponseDto.getCodigoEstadoTituloCobranca())) {
                                            boletoStatusFinais.add(boleto);
                                        } else {
                                            boletoDtos.add(boleto);
                                        }
                                        logEnvioDto.add(new LogEnvio(boletoDto.getNumeroTituloCliente(),TipoEventoEnum.RETORNO_BANCO.getDescricaoEvento(),"Atualizado com sucesso",
                                                boletoResponseDto.getCodigoEstadoTituloCobranca(), TipoEventoEnum.RETORNO_BANCO.getIdTipoEvento(),
                                                SituacaoEnum.PROCESSADA.getIdSituacao(), boletoResponseDto.getCodigoEstadoTituloCobranca()));
                                    }
                                }
                            }

                            if(!boletoStatusFinais.isEmpty()) {
                                List<BoletoStatusFinal> boletosStatusFinais = boletoToBoletoStatusFinais(boletoStatusFinais);
                                boletoStatusFinalRepository.saveAll(boletosStatusFinais);
                                for (BoletoStatusFinal boletoStatusFinal : boletosStatusFinais) {
                                    boolean existeBoleto = boletoStatusFinalRepository.existsById(boletoStatusFinal.getId());
                                    if(existeBoleto) {
                                        boletoRepository.deleteById(boletoStatusFinal.getId());
                                    }
                                }
                                logEnvioRepository.saveAll(logEnvioDto);
                                logEnvioDto.clear();
                            }

                            if (!boletoDtos.isEmpty()) {
                                boletoRepository.saveAll(boletoDtos);
                                boletoDtos.clear();
                                logEnvioRepository.saveAll(logEnvioDto);
                                logEnvioDto.clear();
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    public ArrayList<BoletoResponseDto> filtraBoletos(StatusBoletoFilterRequestDto filter){
        return filtraBoletos(filter,null);
    }
    
    public ArrayList<BoletoResponseDto> filtraBoletosSearch(StatusBoletoFilterSearchRequestDto filter){
        return filtraBoletosSearch(filter,null);
    }
    
    public ArrayList<BoletoResponseDto> filtraBoletosSearch(StatusBoletoFilterSearchRequestDto filter, Pageable pageable){
        List<Boleto> boletolist = boletoCustomSearchRepository.findByRequest(filter,pageable);

        return getFiltraBoletos(boletolist);
    }

    public ArrayList<BoletoResponseDto> filtraBoletos(StatusBoletoFilterRequestDto filter, Pageable pageable){
        List<Boleto> boletolist = boletoCustomRepository.findByRequest(filter,pageable);
        return getFiltraBoletos(boletolist);
    }

	private ArrayList<BoletoResponseDto> getFiltraBoletos(List<Boleto> boletolist) {
		ArrayList<BoletoResponseDto> boletoDtolist = new ArrayList<>();

        if (!boletolist.isEmpty()) {
            for (Boleto boleto : boletolist) {
                BoletoResponseDto boletoResponseDto = new BoletoResponseDto();
                BoletoDto boletoDto = new BoletoDto(boleto);
                boletoDto.setId(boleto.getId());
                boletoDto.setIdapibanco(boleto.getConta().getId());
                boletoDto.setNumeroTituloBeneficiario(boleto.getNumeroTituloBeneficiario());
                boletoDto.setNumeroTituloCliente(boleto.getNumeroTituloCliente());
                boletoDto.setDescricaoStatus(boleto.getStatus());
                boletoResponseDto.setBoleto(boletoDto);
                boletoDtolist.add(boletoResponseDto);
            }
        }

        return boletoDtolist;
	}

    public ArrayList<BoletoResponseDto> filtra100Boletos(ArrayList<StatusBoletoFilterRequestDto> statusBoletoFilterRequestDto){
        ArrayList<BoletoResponseDto> boletoDtolist = new ArrayList<>();

        for (StatusBoletoFilterRequestDto filter : statusBoletoFilterRequestDto) {
            List<Boleto> boletolist = boletoCustomRepository.findByRequest(filter,null);

            if (!boletolist.isEmpty()) {
                for (Boleto boleto : boletolist) {
                    BoletoResponseDto boletoResponseDto = new BoletoResponseDto();
                    BoletoDto boletoDto = new BoletoDto(boleto);
                    boletoDto.setId(boleto.getId());
                    boletoDto.setIdapibanco(boleto.getConta().getId());
                    boletoDto.setNumeroTituloBeneficiario(boleto.getNumeroTituloBeneficiario());
                    boletoDto.setNumeroTituloCliente(boleto.getNumeroTituloCliente());
                    boletoDto.setDescricaoStatus(boleto.getStatus());
                    boletoResponseDto.setBoleto(boletoDto);
                    boletoDtolist.add(boletoResponseDto);
                }
            }
        }

        return boletoDtolist;
    }
    
    public void registraBoletoQueue(Job job) {
        //TODO VALIDAR APÓS A IMPLEMENTAÇÃO DO JPA UMA NOVA SOLUÇÃO
//        List<BoletoProcessorService> boletoProcessorServices = Arrays.asList(new BoletoProcessorService[]{new BancoBrasilProcessorService(),new BancoItauProcessorService()});
//        BoletoProcessorFactory boletoProcessorFactory = new BoletoProcessorFactory(boletoProcessorServices);

        BlockingQueue<Conta> queue = getContasBoletos(StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus());
        Integer count = queue.size();
        RegistraBoletoQueue registraBoletosConta = new RegistraBoletoQueue(queue, boletoProcessorFactory );

        Thread threadRegistra_1 = new Thread(registraBoletosConta);
		threadRegistra_1.setName("MyThread-Registra-Boleto-1");

		Thread threadRegistra_2 = new Thread(registraBoletosConta);
		threadRegistra_2.setName("MyThread-Registra-Boleto-2");

		Thread threadRegistra_3 = new Thread(registraBoletosConta);
		threadRegistra_3.setName("MyThread-Registra-Boleto-3");

		Thread threadRegistra_4 = new Thread(registraBoletosConta);
		threadRegistra_4.setName("MyThread-Registra-Boleto-4");

		threadRegistra_1.start();
		threadRegistra_2.start();
		threadRegistra_3.start();
		threadRegistra_4.start();
        
        try {
			threadRegistra_1.join();
			threadRegistra_2.join();
			threadRegistra_3.join();
			threadRegistra_4.join();
		} catch (InterruptedException e) {
			job.setError(e.getMessage());
			e.printStackTrace();
		}finally {
			if(count > 0 || job.getError() != null) {
	        	job.setQtdRegistro(queue.size());
	        	job.setDataFinal(Timestamp.valueOf(LocalDateTime.now()));
	        	Timestamp proxEx = Timestamp.valueOf(LocalDateTime.now());
				proxEx.setMinutes(proxEx.getMinutes() + JobEnum.valueByTempo(job.getTipo()));
				job.setDataProxExec(proxEx);
	        	job.setStatus(1);
	        	jobRepository.save(job);
	        } else {
	        	jobRepository.deleteById(job.getId());
	        }
		}
    }

    public void alteraBoletoQueue(Job job) {
        //TODO VALIDAR APÓS A IMPLEMENTAÇÃO DO JPA UMA NOVA SOLUÇÃO
//        List<BoletoProcessorService> boletoProcessorServices = Arrays.asList(new BoletoProcessorService[]{new BancoBrasilProcessorService(),new BancoItauProcessorService()});
//        BoletoProcessorFactory boletoProcessorFactory = new BoletoProcessorFactory(boletoProcessorServices);

		BlockingQueue<Conta> queue = getContasBoletos(StatusBoletoEnum.ALTERA_BOLETO.getStatus());
		Integer count = queue.size();
		AlteraBoletoQueue alteraBoletosConta = new AlteraBoletoQueue(queue, boletoProcessorFactory);
		
		Thread threadAlterta_1 = new Thread(alteraBoletosConta);
		threadAlterta_1.setName("MyThread-Altera-Boleto-1");

		Thread threadAlterta_2 = new Thread(alteraBoletosConta);
		threadAlterta_2.setName("MyThread-Altera-Boleto-2");

		Thread threadAlterta_3 = new Thread(alteraBoletosConta);
		threadAlterta_3.setName("MyThread-Altera-Boleto-3");

		Thread threadAlterta_4 = new Thread(alteraBoletosConta);
		threadAlterta_4.setName("MyThread-Altera-Boleto-4");
		
		threadAlterta_1.start();
		threadAlterta_2.start();
		threadAlterta_3.start();
		threadAlterta_4.start();
		
		try {
			threadAlterta_1.join();
			threadAlterta_2.join();
			threadAlterta_3.join();
			threadAlterta_4.join();
		} catch (InterruptedException e) {
			job.setError(e.getMessage());
			e.printStackTrace();
		}finally {
			if(count > 0 || job.getError() != null) {
	        	job.setQtdRegistro(queue.size());
	        	job.setDataFinal(Timestamp.valueOf(LocalDateTime.now()));
	        	Timestamp proxEx = Timestamp.valueOf(LocalDateTime.now());
				proxEx.setMinutes(proxEx.getMinutes() + JobEnum.valueByTempo(job.getTipo()));
				job.setDataProxExec(proxEx);
	        	job.setStatus(1);
	        	jobRepository.save(job);
	        } else {
	        	jobRepository.deleteById(job.getId());
	        }
		}
	}

    public void cancelaBoletoQueue(Job job) {
        //TODO VALIDAR APÓS A IMPLEMENTAÇÃO DO JPA UMA NOVA SOLUÇÃO
//        List<BoletoProcessorService> boletoProcessorServices = Arrays.asList(new BoletoProcessorService[]{new BancoBrasilProcessorService(),new BancoItauProcessorService()});
//        BoletoProcessorFactory boletoProcessorFactory = new BoletoProcessorFactory(boletoProcessorServices);

        BlockingQueue<Conta> queue = getContasBoletos(StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus());
        Integer count = queue.size();
        CancelaBoletoQueue cancelaBoletosConta = new CancelaBoletoQueue(queue, boletoProcessorFactory);
        
        Thread threadCancela_1 = new Thread(cancelaBoletosConta);
		threadCancela_1.setName("MyThread-Altera-Boleto-1");

		Thread threadCancela_2 = new Thread(cancelaBoletosConta);
		threadCancela_2.setName("MyThread-Altera-Boleto-2");

		Thread threadCancela_3 = new Thread(cancelaBoletosConta);
		threadCancela_3.setName("MyThread-Altera-Boleto-3");

		Thread threadCancela_4 = new Thread(cancelaBoletosConta);
		threadCancela_4.setName("MyThread-Altera-Boleto-4");
		
		threadCancela_1.start();
		threadCancela_2.start();
		threadCancela_3.start();
		threadCancela_4.start();
		
		try {
			threadCancela_1.join();
			threadCancela_2.join();
			threadCancela_3.join();
			threadCancela_4.join();
		} catch (InterruptedException e) {
			job.setError(e.getMessage());
			e.printStackTrace();
		}finally {
			if(count > 0 || job.getError() != null) {
	        	job.setQtdRegistro(queue.size());
	        	job.setDataFinal(Timestamp.valueOf(LocalDateTime.now()));
	        	Timestamp proxEx = Timestamp.valueOf(LocalDateTime.now());
				proxEx.setMinutes(proxEx.getMinutes() + JobEnum.valueByTempo(job.getTipo()));
				job.setDataProxExec(proxEx);
	        	job.setStatus(1);
	        	jobRepository.save(job);
	        } else {
	        	jobRepository.deleteById(job.getId());
	        }
		}

    }

    private BlockingQueue<Conta> getContasBoletos(Integer status) {
        List<Integer> idsConta = boletoRepository.findDistinctByStatus(status);

        BlockingQueue<Conta> queue = new LinkedBlockingQueue<>();
        List<Conta> contaList = contaRepository.findAllById(idsConta);

        if (!contaList.isEmpty()) {
            for (Conta conta : contaList) {
                queue.add(conta);
            }
        }

        return queue;
    }

    public void consultaBoletoQueue(Job job) {
    	 
    	int[] statusFinais = {
    			 StatusBoletoEnum.LIQUIDADO.getStatus(), 
    			 StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus(),
                 StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus(),
                 StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus(),
                 StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus(),
                 StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus(), 
                 StatusBoletoEnum.TITULO_CREDITADO.getStatus(),
                 StatusBoletoEnum.ERRO.getStatus(), 
                 StatusBoletoEnum.ALTERA_BOLETO.getStatus()};
    	
         List<Integer> list = Arrays.stream(statusFinais).boxed().collect(Collectors.toList());
    	
         List<Conta> contaList = contaRepository.buscarContasQueTemBoletos(list);
         BlockingQueue<Conta> queue = new LinkedBlockingQueue<>();
         Integer count = contaList.size();
         if (!contaList.isEmpty()) {
             for (Conta conta : contaList) {
                 queue.add(conta);
             }
         }

         //TODO VALIDAR APÓS A IMPLEMENTAÇÃO DO JPA UMA NOVA SOLUÇÃO
//        List<BoletoProcessorService> boletoProcessorServices = Arrays.asList(new BoletoProcessorService[]{new BancoBrasilProcessorService(),new BancoItauProcessorService()});
//        BoletoProcessorFactory boletoProcessorFactory = new BoletoProcessorFactory(boletoProcessorServices);

        ConsultaBoletoQueue consultaBoletosConta = new ConsultaBoletoQueue(queue, boletoProcessorFactory);

        Thread threadAtualizaStatus_1 = new Thread(consultaBoletosConta);
		threadAtualizaStatus_1.setName("MyThread-Atualiza-Status-Boleto-1");

		Thread threadAtualizaStatus_2 = new Thread(consultaBoletosConta);
		threadAtualizaStatus_2.setName("MyThread-Atualiza-Statu-Boleto-2");

		Thread threadAtualizaStatus_3 = new Thread(consultaBoletosConta);
		threadAtualizaStatus_3.setName("MyThread-Atualiza-Statu-Boleto-3");

		Thread threadAtualizaStatus_4 = new Thread(consultaBoletosConta);
		threadAtualizaStatus_4.setName("MyThread-Atualiza-Statu-Boleto-4");
		
		threadAtualizaStatus_1.start();
		threadAtualizaStatus_2.start();
		threadAtualizaStatus_3.start();
		threadAtualizaStatus_4.start();
		
        try {
			threadAtualizaStatus_1.join();
			threadAtualizaStatus_2.join();
			threadAtualizaStatus_3.join();
			threadAtualizaStatus_4.join();
        } catch (InterruptedException e) {
			job.setError(e.getMessage());
			e.printStackTrace();
		}finally {
			if(count > 0 || job.getError() != null) {
	        	job.setQtdRegistro(queue.size());
	        	job.setDataFinal(Timestamp.valueOf(LocalDateTime.now()));
	        	Timestamp proxEx = Timestamp.valueOf(LocalDateTime.now());
				proxEx.setMinutes(proxEx.getMinutes() + JobEnum.valueByTempo(job.getTipo()));
				job.setDataProxExec(proxEx);
	        	job.setStatus(1);
	        	jobRepository.save(job);
	        } else {
	        	jobRepository.deleteById(job.getId());
	        }
		}
    }
    
	public void buscarRetornoBoletoDiario(Job job) {
		int[] statusFinais = { StatusBoletoEnum.LIQUIDADO.getStatus(), StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus(),
				StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus(), StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus(),
				StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus(), StatusBoletoEnum.TITULO_CREDITADO.getStatus() };
		Integer count = 0;
		try {
			List<Integer> list = Arrays.stream(statusFinais).boxed().collect(Collectors.toList());

			Timestamp proxEx = Timestamp.valueOf(LocalDateTime.now());
			proxEx.setMinutes(proxEx.getMinutes() - 1440);

			List<Conta> contas = contaRepository.buscarContasQueTemBoletos(list, proxEx);
			for (Conta conta : contas) {
				List<Boleto> boletos = boletoRepository.buscarBoletosLiquidadosDia(list, proxEx, conta.getId());
				LoginResponseDto loginResponseDto = authentication(conta);
				for (Boleto boleto : boletos) {
					if (boleto.getConta().getId() != null) {
						try {
							count++;
							ResponseEntity<BoletoRetornoDto> response = detalharBoletoQueue(boleto, loginResponseDto);
							BoletoRetornoDto boletoApi = response.getBody();
							boletoApi.setIdboleto(boleto.getId());
							if (boletoApi != null && boletoApi.getCodigoEstadoTituloCobranca() != null) {
								insereRetornoBancoLogEnvio(response, boleto, boletoApi);
								updateStatusBoleto(boleto, boletoApi);
							}
						} catch (FailedAuthenticationException se) {
							loginResponseDto = authentication(conta);
							log.error(
									"Erro ao obter detalhes do boleto junto ao banco. - Número Título Cliente: {} [Método: detalharBoleto]",
									isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
							log.error(se.getMessage(), se);
						} catch (HttpServerErrorException hhtp) {
							log.error(
									"Erro ao obter detalhes do boleto junto ao banco. - Número Título Cliente: {} [Método: detalharBoleto]",
									isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
							log.error(hhtp.getMessage(), hhtp);
						} catch (Exception e) {
							log.error(
									"Erro ao atualizar status do boleto. - Número Título Cliente: {} [Método: atualizaStatusBoletos]",
									isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
							log.error(e.getMessage(), e);
							BoletoDto boletoDto = new BoletoDto(boleto);
							if (!StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus()
									.equals(boletoDto.getStatus())) {
								LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e,
										TipoEventoEnum.RETORNO_BANCO);
								if (logEnvioDto != null) {
									logEnvioService.salvar(logEnvioDto);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			job.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if (count > 0 || job.getError() != null) {
				job.setQtdRegistro(count);
				job.setDataFinal(Timestamp.valueOf(LocalDateTime.now()));
				Timestamp proxEx = Timestamp.valueOf(LocalDateTime.now());
				proxEx.setMinutes(proxEx.getMinutes() + JobEnum.valueByTempo(job.getTipo()));
				job.setDataProxExec(proxEx);
				job.setStatus(1);
				jobRepository.save(job);
			} else {
				jobRepository.deleteById(job.getId());
			}
		}
	}

    public ArrayList<BoletoResponseDto> registraBoletoBancoBrasil(Integer conta) {
        return registraBoletoBancoBrasil(null, conta);
    }

    @Transactional
    public ArrayList<BoletoResponseDto> registraBoletoBancoBrasil(ArrayList<BoletoDto> boletosDto, Integer conta) {
        ArrayList<BoletoResponseDto> responseBoletoDto = new ArrayList<>();
        List<BoletoDto> boletoDtolist = new ArrayList<>();

        if (boletosDto == null && conta != null) {
            boletoDtolist = buscaPorStatus(StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus(), conta);
        } else {
            boletoDtolist = boletosDto;
        }

        if (boletoDtolist != null && !boletoDtolist.isEmpty()) {
            for (BoletoDto boletoDto : boletoDtolist) {
                try {
                    ResponseEntity<BoletoResponseDto> response = envioBoletoApibanco(boletoDto);
                    ArrayList<BoletoDto> boletoDtos = new ArrayList<>();

                    if (response != null && HttpStatus.CREATED.equals(response.getStatusCode())) {
                        boletoDto.setCodigoLinhaDigitavel(response.getBody().getBoleto().getCodigoLinhaDigitavel());
                        boletoDto.setStatus(StatusBoletoEnum.NORMAL.getStatus());
                        boletoDto.setStatusBanco(StatusBoletoEnum.NORMAL.getStatus());
                        boletoDto.setUrl(response.getBody().getBoleto().getUrl());
                        boletoDto.setTxId(response.getBody().getBoleto().getTxId());
                        boletoDto.setEmv(response.getBody().getBoleto().getEmv());
                        boletoDto.setDataRegistroBanco(DateUtil.getDataAtual());

                        boletoDtos.add(boletoDto);
                        StringBuilder stacktrace = new StringBuilder().append("Status ").append(response.getStatusCodeValue()).append(": Criado com sucesso");
                        ArrayList<BoletoResponseDto> boletoResponseDto = alteraBoletosRetornoBanco(boletoDtos);
                        boletoResponseDto.get(0).setMessage(stacktrace.toString());
                        responseBoletoDto.addAll(boletoResponseDto);

                        LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.EMISSAO.getDescricaoEvento(), stacktrace.toString(), boletoDto.getStatus(), TipoEventoEnum.EMISSAO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.NORMAL.getStatus());
                        logEnvioService.salvar(logEnvioDto);

                    } else {
                        BoletoResponseDto boletoResponseDto = new BoletoResponseDto();
                        boletoResponseDto.setBoleto(boletoDto);
                        boletoResponseDto.setMessage(response.getBody().getMessage());
                        responseBoletoDto.add(boletoResponseDto);
                        boletoDtos.add(boletoDto);
                    }
                } catch (Exception e) {
                    log.error("Erro ao registrar boleto no banco. - Número Título Cliente: {} [Método: registraBoletoBancoBrasil]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
                    log.error(e.getMessage(), e);
                    LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e, TipoEventoEnum.EMISSAO);
                    if (logEnvioDto != null) {
                        logEnvioService.salvar(logEnvioDto);
                    }
                }
            }
        }
        return responseBoletoDto;
    }

    @Transactional
    public void cancelaBoletoBancoBrasil(Integer conta) {
        List<BoletoDto> boletoDtoCancelalist = buscaPorStatus(StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus(), conta);

        if (!boletoDtoCancelalist.isEmpty()) {
            for (BoletoDto boletoDto : boletoDtoCancelalist) {
                try {
                    ResponseEntity<CancelamentoBoletoResponseDto> response = cancelaBoletoApibanco(boletoDto);
                    ArrayList<BoletoDto> boletoDtos = new ArrayList<BoletoDto>();
                    if (response.getStatusCode().equals(HttpStatus.OK)) {
                      //  cancelaBoletoApibanco(boletoDto);
                        boletoDto.setStatus(StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());
                        boletoDto.setStatusBanco(StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());
                        boletoDtos.add(boletoDto);
                        alteraBoletosRetornoBanco(boletoDtos);

                        StringBuilder stacktrace = new StringBuilder().append("Status ").append(response.getStatusCodeValue()).append(": ").append("Executado com sucesso");
                        LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.BAIXA_CANCELAMENTO.getDescricaoEvento(), stacktrace.toString(), boletoDto.getStatus(), TipoEventoEnum.BAIXA_CANCELAMENTO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());
                        logEnvioService.salvar(logEnvioDto);
                    } else {
                        LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, response, TipoEventoEnum.BAIXA_CANCELAMENTO);
                        if (logEnvioDto != null) {
                            logEnvioService.salvar(logEnvioDto);
                        }
                        boletoDtos.add(boletoDto);
                        alteraBoletosRetornoBanco(boletoDtos);
                    }
                } catch (Exception e) {
                    log.error("Erro ao cancelar boleto no banco. - Número Título Cliente: {} [Método: cancelaBoletoBancoBrasil]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
                    log.error(e.getMessage(), e);
                    LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e, TipoEventoEnum.EMISSAO);
                    if (logEnvioDto != null) {
                        logEnvioService.salvar(logEnvioDto);
                    }
                }
            }
        }
    }

    private void validaCamposFiltroBoletos(BoletoPaginadosFilterRequestDto filter){
        if ((filter.getDataEmissaoInicial() == null || filter.getDataEmissaoInicial().isBlank())
                && (filter.getDataVencimentoInicial() == null || filter.getDataVencimentoInicial().isBlank())) {
            throw new BadRequestException(
                    "A data de vencimento ou a data de emissão deve ter pelo menos uma data inicial obrigatória.");
        } else {
            if ((filter.getDataEmissaoInicial() != null && !filter.getDataEmissaoInicial().isBlank())
                    && (filter.getDataEmissaoFinal() != null && !filter.getDataEmissaoFinal().isBlank())) {
                Timestamp dataEmissaoInicial = DateUtil.convertStringToTimestamp(filter.getDataEmissaoInicial());
                Timestamp dataEmissaoFinal = DateUtil.convertStringToTimestamp(filter.getDataEmissaoFinal());

                if (dataEmissaoInicial.compareTo(dataEmissaoFinal) > 0) {
                    throw new BadRequestException("A data de emissão inicial deve ser anterior a data de emissão final.");
                }
            }
            if ((filter.getDataVencimentoInicial() != null && !filter.getDataVencimentoInicial().isBlank())
                    && (filter.getDataVencimentoFinal() != null && !filter.getDataVencimentoFinal().isBlank())) {
                Timestamp dataVencimentoInicial = DateUtil.convertStringToTimestamp(filter.getDataVencimentoInicial());
                Timestamp dataVencimentoFinal = DateUtil.convertStringToTimestamp(filter.getDataVencimentoFinal());

                if (dataVencimentoInicial.compareTo(dataVencimentoFinal) > 0) {
                    throw new BadRequestException("A data de vencimento inicial deve ser anterior a data de vencimento final.");
                }
            }
        }

        if (filter.getIdApiBanco() == null || filter.getIdApiBanco().isEmpty()) {
            throw new BadRequestException("O Campo IdApiBanco é obrigatório.");
        }
    }

    public Integer verificaStatusBanco(Integer status) {
        if (!StatusBoletoEnum.ERRO.getStatus().equals(status) && !StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus().equals(status) 
        		&& !StatusBoletoEnum.ALTERA_BOLETO.getStatus().equals(status)) {
            return status;
        } else {
            return StatusBoletoEnum.NORMAL.getStatus();
        }
    }
    
    public Boolean isStatusFinal(Integer status) {
        Integer[] statusFinais = {StatusBoletoEnum.LIQUIDADO.getStatus(), StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus(),
                StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus(), StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus(),
                StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus(), StatusBoletoEnum.TITULO_CREDITADO.getStatus(),
                StatusBoletoEnum.ERRO.getStatus(), StatusBoletoEnum.ALTERA_BOLETO.getStatus()};

        for (Integer statusFinal : statusFinais) {
            if (statusFinal.equals(status)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isStatusFinal(Integer status, Integer bancoId) {
    	List<Integer> statusFinais = new ArrayList<Integer>();
    	if (BancoEnum.BANCO_DO_BRASIL.getId().equals(bancoId)) {
    		statusFinais = statusFinaisBoletoBB();			
		}
    	if(BancoEnum.ITAU.getId().equals(bancoId)) {
    		statusFinais = statusFinaisBoletoItau();
    	}

        for (Integer statusFinal : statusFinais) {
            if (statusFinal.equals(status)) {
                return true;
            }
        }
        return false;
    }
    
    private List<Integer> statusFinaisBoletoBB(){
    	List<Integer> statusFinais = new ArrayList<>();
    	statusFinais.add(StatusBoletoEnum.LIQUIDADO.getStatus());
    	statusFinais.add(StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());
    	statusFinais.add(StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus());
    	statusFinais.add(StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus());
    	statusFinais.add(StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus());
    	statusFinais.add(StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus());
    	statusFinais.add(StatusBoletoEnum.TITULO_CREDITADO.getStatus());
    	statusFinais.add(StatusBoletoEnum.ERRO.getStatus());
    	statusFinais.add(StatusBoletoEnum.ALTERA_BOLETO.getStatus());
    	statusFinais.add(StatusBoletoEnum.TITULO_COM_PENDENCIA_CARTORIO.getStatus());
    	statusFinais.add(StatusBoletoEnum.TITULO_PROTESTADO_MANUAL.getStatus());
    	statusFinais.add(StatusBoletoEnum.TITULO_PROTESTADO_AGUARDANDO_BAIXA.getStatus());
    	statusFinais.add(StatusBoletoEnum.SUCESSO.getStatus());
    	
    	return statusFinais;
    }

    private List<Integer> statusFinaisBoletoItau(){
    	List<Integer> statusFinais = new ArrayList<>();
    	statusFinais.add(StatusBoletoItauEnum.PAGO.getStatus());
    	//statusFinais.add(StatusBoletoItauEnum.PAGAMENTO_DEVOLVIDO.getStatus());
    	statusFinais.add(StatusBoletoItauEnum.BAIXADO_PELO_BANCO.getStatus());
    	
    	return statusFinais;
    }
    
    public LoginResponseDto authentication(Conta conta) {
        LoginResponseDto loginResponseDto;

        try {
            BancoEnum bancoEnum = BancoEnum.valueById(conta.getBanco().getId());
            LoginDto loginDto = new LoginDto(conta.getClientid(), conta.getClientsecret(), bancoEnum, conta.getId(), null, null);

            loginResponseDto = authenticationFactory.getAuthentication(bancoEnum).authentication(loginDto);

        } catch (Exception e) {
            log.error("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente. [Método: authentication]");
            log.error(e.getMessage(), e);
            throw new FailedAuthenticationException(
                    "As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente");
        }

        if (loginResponseDto.getAccess_token() == null) {
            log.error("Falha na autenticação das credenciais, tente novamente. Token de acesso nulo. [Método: authentication]");
            throw new FailedAuthenticationException("Falha na autenticação das credenciais, tente novamente");
        }

        return loginResponseDto;
    }

    public List<BoletoDto> buscaBoletosPorStatus(Integer status, Integer idConta) {
        List<Boleto> boletolist = boletoRepository.findByStatusAndConta(status, idConta);

        List<BoletoDto> boletoDtolist = new ArrayList<>();

        if (!boletolist.isEmpty()) {
            for (Boleto boleto : boletolist) {

                Optional<Conta> conta = contaRepository.findById(boleto.getConta().getId());

                if (conta.isPresent() && "S".equals(conta.get().getStatusapi())) {

                    BoletoDto boletoDto = new BoletoDto(boleto);
                    boletoDto.setStatusBanco(verificaStatusBanco(status));

                    boletoDtolist.add(boletoDto);
                }
            }
        }

        return boletoDtolist;
    }

    public Page<BoletoResponseDto> buscaBoletosPaginadosFiltro(BoletoPaginadosFilterRequestDto filter, Pageable pageable){
        validaCamposFiltroBoletos(filter);

        Page<Boleto> pageBoletos = boletoRepository.boletosPaginadosFiltro(filter,pageable);

        Page<BoletoResponseDto> page = new PageImpl<>(getFiltraBoletos(pageBoletos.getContent()),pageable,pageBoletos.getTotalElements());
        return page;
    }
    
    public ArrayList<BoletoResponseDto> buscaBoletosPaginadosSearchFiltro(StatusBoletoFilterSearchRequestDto filter, Pageable pageable){
        try{
            ArrayList<BoletoResponseDto> boletos = filtraBoletosSearch(filter);
            return boletos;
        }catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Erro ao salvar Parceiro");
        }
    }
    
    public List<BoletoDto> buscaPorConta(Integer idConta){
    	List<Boleto> boletolist = boletoRepository.findByConta(idConta);
        ArrayList<BoletoDto> boletoDtolist = new ArrayList<BoletoDto>();   
        if (!boletolist.isEmpty()) {
			for (Boleto boleto : boletolist) {
				boletoDtolist.add(new BoletoDto(boleto));
			}
		}
        return boletoDtolist;
    }
    
    public LocalDateTime buscaUltimaDataAtualizacaoPorConta(Integer idConta) {
    	LocalDateTime ultimaData = boletoRepository.findUltimaDataAtualizacao(idConta);
    	if (isNull(ultimaData)) {
    		ultimaData = boletoRepository.findUltimaDataCriacao(idConta);
		}
    	return ultimaData;
    }

    public ArrayList<BoletoResponseDto> cancelaBoleto(ArrayList<BoletoDto> boletoDtos){
        try {
            ArrayList<BoletoDto> boletos = new ArrayList<>();
            for (BoletoDto boletoReq : boletoDtos) {
                BoletoDto boletoDto = findByNumeroTituloCliente(boletoReq.getNumeroTituloCliente());
                if(boletoDto != null && !isStatusFinal(boletoDto.getStatus())) {
                    LogEnvioDto logEnvioDto = new LogEnvioDto(boletoReq.getNumeroTituloCliente(), TipoEventoEnum.BAIXA_CANCELAMENTO.getDescricaoEvento(), "", boletoDto.getStatus(), TipoEventoEnum.BAIXA_CANCELAMENTO.getIdTipoEvento(), SituacaoEnum.PENDENTE_ENVIO.getIdSituacao(), boletoDto.getStatusBanco());
                    logEnvioService.salvar(logEnvioDto);
                    boletoDto.setStatus(StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus());
                    boletos.add(boletoDto);
                }
            }
            return processaBoletos(boletos);
        } catch (FailedAuthenticationException e) {
            log.error(e.getMessage(), e);
            throw new FailedAuthenticationException(e.getMessage());
        } catch (Exception e) {
            throw new NotFoundException("Tivemos um erro ao cancelar os boletos.");
        }
    }

    public ArrayList<BoletoResponseDto> registraBoleto(ArrayList<BoletoDto> boletoDtos){
        ArrayList<BoletoResponseDto> resp = new ArrayList<>();
        try {
            for (BoletoDto boletoDto : boletoDtos) {
                ArrayList<BoletoDto> boletoDtoAux = new ArrayList<>();
                boletoDtoAux.add(boletoDto);
                try {
                    resp.addAll(processaBoletos(boletoDtoAux));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    BoletoResponseDto response = new BoletoResponseDto();
                    response.setMessage(e.getMessage());
                    response.setBoleto(boletoDto);
                    resp.add(response);
                }
            }
            if (!resp.isEmpty()) {
                ArrayList<BoletoResponseDto> response = new ArrayList<>();
                response.addAll(resp);
                resp.clear();
                for (BoletoResponseDto boletoResponseDto : response) {
                    ArrayList<BoletoDto> boletoDtolist = new ArrayList<>();
                    boletoDtolist.add(boletoResponseDto.getBoleto());
                    try {
                        resp.addAll(registraBoletoBancoBrasil(boletoDtolist,null));
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        BoletoResponseDto boletoResponse = new BoletoResponseDto();
                        boletoResponse.setMessage("Erro ao salvar boleto: " + e.getMessage());
                        boletoResponse.setBoleto(boletoResponseDto.getBoleto());
                        resp.add(boletoResponse);
                    }
                }
            }
            return resp;
        } catch (FailedAuthenticationException e) {
            log.error(e.getMessage(), e);
            throw new FailedAuthenticationException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    } 
    
	public String atualizarStatusBoletoPorConta(AtualizaBoletosRequestDto filter, Pageable pageable) {
		String status = "";
		int[] statusFinais = { StatusBoletoEnum.LIQUIDADO.getStatus(), StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus(),
				StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus(),
				StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus(), StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus(),
				StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus(), StatusBoletoEnum.TITULO_CREDITADO.getStatus(),
				StatusBoletoEnum.ERRO.getStatus(), StatusBoletoEnum.ALTERA_BOLETO.getStatus() };
		List<Integer> list = Arrays.stream(statusFinais).boxed().collect(Collectors.toList());
		List<Boleto> boletos = new ArrayList<Boleto>();
		Integer totalBoletos = 0;
		Optional<Conta> conta = null;
		try {
			if (filter.getIdApiBanco() == null) {
				if (filter.getNumeroTituloCliente() != null || !filter.getNumeroTituloCliente().isEmpty()) {
					boletos.add(boletoRepository.findBynumeroTituloCliente(filter.getNumeroTituloCliente()));
				} else if (filter.getNumeroTituloBeneficiario() != null || filter.getNumeroTituloBeneficiario().isEmpty()) {
					boletos.add(boletoRepository.findByNumeroTituloBeneficiario(filter.getNumeroTituloBeneficiario()));
				}
				if(boletos != null && !boletos.isEmpty()) {
					conta = contaRepository.findById(boletos.get(0).getConta().getId());
				}
			} else {
				conta = contaRepository.findById(filter.getIdApiBanco());
				if (filter.getNumeroTituloCliente() != null || !filter.getNumeroTituloCliente().isEmpty()) {
					boletos.add(boletoRepository.findBynumeroTituloCliente(filter.getNumeroTituloCliente()));
				} else if (filter.getNumeroTituloBeneficiario() != null || filter.getNumeroTituloBeneficiario().isEmpty()) {
					boletos.add(boletoRepository.findByNumeroTituloBeneficiario(filter.getNumeroTituloBeneficiario()));
				} else {
					boletos = getStatusBoletoNotInStatus(list, filter.getIdApiBanco());
				}
			}
			if (conta.isPresent()) {
				LoginResponseDto loginResponseDto = authentication(conta.get());
				if(filter.getTipConsulta() == 0) {
					for (Boleto boleto : boletos) {
						try {
							enviaBoletoStatus(conta.get(), loginResponseDto, boleto);
							totalBoletos +=totalBoletos;
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				} else {
					try {
						Map<String, Boleto> boletoMap = new HashMap<String, Boleto>();
						for (Boleto boleto : boletos) {
							boletoMap.put(boleto.getNumeroTituloCliente(), boleto);
						}
						Timestamp dtInicial = Timestamp.valueOf(LocalDateTime.now());
						dtInicial.setMinutes(dtInicial.getMinutes() - 5760);
						totalBoletos = envioStatus(authentication(conta.get()), "B", conta.get(), DateUtil.convertTimeStampToString(dtInicial),
								boletoMap,true);
					} catch (HttpServerErrorException e) {
						log.error(e.getMessage(), e);
						if (HttpStatus.INTERNAL_SERVER_ERROR.equals(e.getStatusCode())) {
							return "***** CONTA ID: " + conta.get().getId() + " AGENCIA : " + conta.get().getCodage()
									+ " CONTA : " + conta.get().getCodctabco() + " ERROR" + e.getMessage();
						}
					} catch (Exception ee) {
						log.error(ee.getMessage(), ee);
					}
				}
			}
			status = "Detalhes do boleto junto ao banco. " + totalBoletos;
		} catch (Exception e) {
			status = "Erro ao obter detalhes do boleto junto ao banco.";
			log.error(status);
		}

		return status;
	}

    public void enviaBoletoStatus(Conta conta, LoginResponseDto loginResponseDto, Boleto boleto) throws Exception,HttpClientErrorException,FailedAuthenticationException ,HttpServerErrorException{
		if (boleto.getConta().getId() != null) {	        
	            ResponseEntity<BoletoRetornoDto> response = detalharBoletoQueue(boleto, loginResponseDto);
	            BoletoRetornoDto boletoApi = response.getBody();
	            boletoApi.setIdboleto(boleto.getId());
	            if (boletoApi != null && boletoApi.getCodigoEstadoTituloCobranca() != null && boletoApi.getCodigoEstadoTituloCobranca() != boleto.getStatus().getStatus()) {
	            	insereRetornoBancoLogEnvio(response, boleto, boletoApi);
	            	updateStatusBoleto(boleto, boletoApi);
	            }
	          
		    }
	}
    
    public void atualizaBoletosBaixadosDiariamente2(Job job) {

		Integer count = 0;
		try {
			List<Integer> statusFinais = new ArrayList<Integer>();
			statusFinais.add(StatusBoletoEnum.LIQUIDADO.getStatus());
			statusFinais.add(StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());
			statusFinais.add(StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus());
			statusFinais.add(StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus());
			statusFinais.add(StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus());
			statusFinais.add(StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus());
			statusFinais.add(StatusBoletoEnum.TITULO_CREDITADO.getStatus());
			statusFinais.add(StatusBoletoEnum.ERRO.getStatus());
			statusFinais.add(StatusBoletoEnum.ALTERA_BOLETO.getStatus());
			
			List<Conta> contas = contaRepository.buscarContasQueTemBoletos(statusFinais);
			for (Conta conta : contas) {
				List<Boleto> boletos = boletoRepository.findByBoletosPendentes(statusFinais, conta.getId());
				Map<String, Boleto> boletoMap = new HashMap<String, Boleto>();
				count = count + boletos.size();
				for (Boleto boleto : boletos) {
					boletoMap.put(boleto.getNumeroTituloCliente(), boleto);
				}
				try {
					Timestamp dtInicial = Timestamp.valueOf(LocalDateTime.now());
					dtInicial.setMinutes(dtInicial.getMinutes() - 1440);
					job.setDataProxExec(dtInicial);
					envioStatus(authentication(conta), "B", conta, DateUtil.convertTimeStampToString(dtInicial),
							boletoMap,true);
				} catch (HttpServerErrorException e) {
					log.error(e.getMessage(), e);
					if (HttpStatus.INTERNAL_SERVER_ERROR.equals(e.getStatusCode())) {
						System.out.println("***** CONTA ID: " + conta.getId() + " AGENCIA : " + conta.getCodage()
								+ " CONTA : " + conta.getCodctabco() + " ERROR" + e.getMessage());
					}
				} catch (Exception ee) {
					log.error(ee.getMessage(), ee);
				}
			}
		} catch (Exception e) {
			job.setError(e.getMessage());
			e.printStackTrace();
		} finally {
			if (count > 0 || job.getError() != null) {
				job.setQtdRegistro(count);
				job.setDataFinal(Timestamp.valueOf(LocalDateTime.now()));
				Timestamp proxEx = Timestamp.valueOf(LocalDateTime.now());
				proxEx.setMinutes(proxEx.getMinutes() + JobEnum.valueByTempo(job.getTipo()));
				job.setDataProxExec(proxEx);
				job.setStatus(1);
				jobRepository.save(job);
			} else {
				jobRepository.deleteById(job.getId());
			}
		}
	} 
    
	public BoletoRetornoDto getBoletoBanco(StatusBoletoFilterRequestDto filter) {
		BoletoRetornoDto boletoApi = new BoletoRetornoDto();
		try {
			Optional<Conta> conta = contaRepository.findById(filter.getConta());
			Boleto boleto = boletoRepository.findBynumeroTituloCliente(filter.getNumeroTituloCliente());
			ResponseEntity<BoletoRetornoDto> responses = detalharBoletoQueue(boleto, authentication(conta.get()));
			boletoApi = responses.getBody();
			boletoApi.setIdboleto(boleto.getId());
			if (boletoApi != null && boletoApi.getCodigoEstadoTituloCobranca() != null) {
				updateStatusBoleto(boleto, boletoApi);
			}
		} catch (HttpClientErrorException http) {
			log.error(
					"Erro ao obter detalhes do boleto junto ao banco. - Número Título Cliente: {} [Método: detalharBoleto]");
			log.error(http.getMessage(), http);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boletoApi;
	}
	
	@Transactional
	public StatusBoletoResponseDto getStatusPorData(StatusContaRequestDto statusDto) {
		
		StatusBoletoResponseDto statusBoletoDtolist =  new StatusBoletoResponseDto();
		ArrayList<BoletoResponseDto> boletoDtolist = new ArrayList<>();
		
		LocalDateTime dataAtualConsulta = DateUtil.getDataAtual();	
		
		List<Integer> status = statusAptosParaFinalizar();
		
		try {
			
			LocalDateTime dtUlConsul = statusDto.getDataUltimaAtualizacao() != null ? statusDto.getDataUltimaAtualizacao().toLocalDateTime() : null;
			if (dtUlConsul == null) {
				dtUlConsul = boletoRepository.findDataPrimeiroBoletoRegistrado(statusDto.getIdApiBanco(), status);
				if (dtUlConsul == null) {
					dtUlConsul = dataAtualConsulta;
				}
			}

			List<Boleto> boletolist = boletoRepository.findBoletosPorData(status, statusDto.getIdApiBanco(),
					DateUtil.convertLocalDateTimeToString(dtUlConsul, Constantes.FORMATO_DATA_PADRAO_MS));

			if (!boletolist.isEmpty()) {
				for (Boleto boleto : boletolist) {
					BoletoResponseDto boletoDto = new BoletoResponseDto();
					boletoDto.setBoleto(new BoletoDto(boleto));
					boletoDtolist.add(boletoDto);
					if (dataAtualConsulta.isAfter(boleto.getAudit().getDataUpdated())) {
						dtUlConsul = boleto.getAudit().getDataUpdated();
					}
				}
			}
			statusBoletoDtolist.setBoletos(boletoDtolist);
			statusBoletoDtolist.setDataUltimaAtualizacaoStatusConta(DateUtil.convertLocalDateTimeToString(dtUlConsul, Constantes.FORMATO_DATA_PADRAO_MS));
			
			if(boletolist.isEmpty()) {
				statusBoletoDtolist.setDataUltimaAtualizacaoStatusConta(DateUtil.convertLocalDateTimeToString(dataAtualConsulta, Constantes.FORMATO_DATA_PADRAO_MS));
			}
			
			return statusBoletoDtolist;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new NotFoundException("Não foi possível realizar a busca de boletos que tiveram alteração de status.");
		}
	}

	private List<Integer> statusAptosParaFinalizar() {
		List<Integer> status = new ArrayList<>();
		status.add(1);
		status.add(6);
		status.add(11);
		status.add(12);
		status.add(16);
		return status;
	}

    public Page<BoletoPaginadosDto> buscaBoletosFiltrados(BoletoPaginadosFilterRequestDto filter, Pageable pageable){
        validaCamposFiltroBoletos(filter);

        Page<Boleto> pageBoletos = boletoRepository.boletosPaginadosFiltro(filter,pageable);

        Page<BoletoPaginadosDto> page = new PageImpl<>(boletoMapper.toListEntityListBoletoPaginadosDto(pageBoletos.getContent()),pageable,pageBoletos.getTotalElements());
        return page;
    }
    
    public Integer buscaBancoPorNumeroTituloCliente(String numeroTituloCliente) {
    	return boletoRepository.buscaBancoPorNumeroTituloCliente(numeroTituloCliente);
    }
    
    public static List<BoletoStatusFinal> boletoToBoletoStatusFinais(List<Boleto> boletos) {
		
		List<BoletoStatusFinal> listaStatusFinais = new ArrayList<>();
		
		for (Boleto boleto : boletos) {
			BoletoStatusFinal newBoleto = new BoletoStatusFinal(boleto);
			listaStatusFinais.add(newBoleto);
		}
		
		return listaStatusFinais;
	}
}