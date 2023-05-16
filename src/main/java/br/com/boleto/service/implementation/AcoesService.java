package br.com.boleto.service.implementation;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.AcaoFilterSearchRequestDto;
import br.com.boleto.persistence.dtos.AcaoRetornoDto4;
import br.com.boleto.persistence.dtos.AcaoRetornoDto5;
import br.com.boleto.persistence.dtos.Acoes2Dto;
import br.com.boleto.persistence.dtos.Acoes3Dto;
import br.com.boleto.persistence.dtos.Acoes4Dto;
import br.com.boleto.persistence.dtos.Acoes5Dto;
import br.com.boleto.persistence.dtos.Acoes6Dto;
import br.com.boleto.persistence.dtos.AcoesDto;
import br.com.boleto.persistence.dtos.AcoesResponseDto;
import br.com.boleto.persistence.dtos.AcoesResponseDto2;
import br.com.boleto.persistence.dtos.AcoesResponseDto3;
import br.com.boleto.persistence.dtos.AcoesResponseDto4;
import br.com.boleto.persistence.dtos.Ordens2Dto;
import br.com.boleto.persistence.dtos.OrdensResponse2Dto;
import br.com.boleto.persistence.entity.Acoes;
import br.com.boleto.persistence.entity.Erros;
import br.com.boleto.persistence.entity.Estrategias;
import br.com.boleto.persistence.entity.EstrategiasPorAcao;
import br.com.boleto.persistence.entity.Ordens;
import br.com.boleto.persistence.mapper.AcoesMapper;
import br.com.boleto.persistence.repository.AcoesCustomSearchRepository;
import br.com.boleto.persistence.repository.AcoesRepository;
import br.com.boleto.persistence.repository.OrdensRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AcoesService {
	
	@Autowired
	private AcoesRepository bancoRepository;
	
	@Autowired
	private OrdensRepository ordensRepository;
	
	@Autowired
	private EstrategiaService estrategiaService;
	
	@Autowired
	private BreakevenService breakevenService;
	
	@Autowired
	private ErrosService errosService;
	
	@Autowired
	private AcoesCustomSearchRepository acoesCustomSearchRepository;
	
	@Autowired
	private EstrategiasPorAcaoService estrategiasPorAcaoService;

	@Autowired
	private AcoesMapper bancoMapper;

	public AcoesResponseDto pesquisaBancoPorId(Integer id) {
		ArrayList<AcoesDto> bancos = new ArrayList<>();
		AcoesResponseDto bancoResponseDto = new AcoesResponseDto();
		Optional<Acoes> bancoOpt = bancoRepository.findById(id);

		if (bancoOpt.isPresent()) {
			bancos.add(bancoMapper.toDtoAcoes(bancoOpt.get()));
			bancoResponseDto.setBanco(bancos);
			return bancoResponseDto;
		}
		log.info("Banco não existe na base de dados - Id: {} [Método: pesquisar]", isNull(id) ? "Nulo" : id.toString());
		throw new NotFoundException("Banco não existe na base de dados");
	}

	public AcoesResponseDto buscaTodosBancos() {
		AcoesResponseDto bancoResponseDto = new AcoesResponseDto();
		bancoResponseDto.setBanco(bancoMapper.toDtoListAcoes(bancoRepository.findAll()));

		return bancoResponseDto;
	}
	
	public ArrayList<AcoesResponseDto2>  findByStatus(String tipo) {
		ArrayList<AcoesResponseDto2> bancoResponseDto = new ArrayList<AcoesResponseDto2>();
		for (Acoes2Dto acoesResponseDto : bancoMapper.toDtoListAcoes2(bancoRepository.findByStatus(tipo))) {
			AcoesResponseDto2 response = new AcoesResponseDto2();
			acoesResponseDto.setEstrategia(estrategiaService.getEstrategiasString(acoesResponseDto.getId()));
			Double valor = acoesResponseDto.getValoracaoatual();
			if(valor != null && valor > 0) {
				if("V".equals(acoesResponseDto.getTipo())) {
					acoesResponseDto.setGain(valor - ((valor * 10)/100));
					acoesResponseDto.setLoss(valor + ((valor * 5)/100));
				} else {
					acoesResponseDto.setGain(valor + ((valor * 10)/100));
					acoesResponseDto.setLoss(valor - ((valor * 5)/100));
				}
			} 
			List<EstrategiasPorAcao> isEstrategia = estrategiasPorAcaoService.findEstrategiaByAcaoId(acoesResponseDto.getId());
			acoesResponseDto.setQtdEstrategia(isEstrategia!= null && !isEstrategia.isEmpty() ? isEstrategia.size() : 0);
			acoesResponseDto.setValorcompra(valor);
			response.setBanco(acoesResponseDto);
			bancoResponseDto.add(response);
		}
		return bancoResponseDto;
	}
	
	public ArrayList<OrdensResponse2Dto>  findBuscarOrdensAProcessar(Integer tipo) {
		ArrayList<OrdensResponse2Dto> bancoResponseDto = new ArrayList<OrdensResponse2Dto>();
		
		for (Ordens acoesResponseDto2 : ordensRepository.findBuscarOrdensAProcessar(tipo)) {
			Ordens2Dto acoesResponseDto = new Ordens2Dto(acoesResponseDto2);
			OrdensResponse2Dto response = new OrdensResponse2Dto();
			acoesResponseDto.setEstrategia(estrategiaService.getEstrategiasString(acoesResponseDto.getId()));
			Double valor = acoesResponseDto.getValoracaoatual();
			if(valor != null && valor > 0) {
				if("V".equals(acoesResponseDto.getTipo())) {
					acoesResponseDto.setGain(valor - ((valor * 10)/100));
					acoesResponseDto.setLoss(valor + ((valor * 5)/100));
				} else {
					acoesResponseDto.setGain(valor + ((valor * 10)/100));
					acoesResponseDto.setLoss(valor - ((valor * 5)/100));
				}
			} 
			List<EstrategiasPorAcao> isEstrategia = estrategiasPorAcaoService.findEstrategiaByAcaoId(acoesResponseDto.getId());
			acoesResponseDto.setQtdEstrategia(isEstrategia!= null && !isEstrategia.isEmpty() ? isEstrategia.size() : 0);
			acoesResponseDto.setValorcompra(valor);
			response.setBanco(acoesResponseDto);
			bancoResponseDto.add(response);
		}
		return bancoResponseDto;
	}

	@Transactional
	public AcoesResponseDto insert(Acoes2Dto filter) {
		AcoesDto aad = new AcoesDto(filter); 
		AcoesResponseDto bancoResponseDto = new AcoesResponseDto();
		List<Acoes> acoes = bancoRepository.findDistinctByAcoes(aad.getAcao());
		//SE NAO EXISTIR INSERI A ESTRATEGIA
		Estrategias est2 = insertEstragegia(filter);
		if(acoes == null || acoes.isEmpty()) {
			Acoes aab = bancoRepository.save(bancoMapper.toDtoAcoes(aad));
			//INSERI A PRIMEIRA OPÇÂO
			insertEstrategiasPorAcao(filter, est2, aab);
			AcoesDto aa = bancoMapper.toDtoAcoes(aab); 
			bancoResponseDto.setBanco(new ArrayList<AcoesDto>());
			bancoResponseDto.getBanco().add(aa);			
		} else {
			//Calcula 
			AcoesDto aa = inserirAlterarEstrategiasPorAcao(filter, est2, bancoMapper.toDtoAcoes(acoes.get(0)));
			Acoes aab = bancoRepository.save(bancoMapper.toDtoAcoes(aa));
			
			bancoResponseDto.setBanco(new ArrayList<AcoesDto>());
			bancoResponseDto.getBanco().add( bancoMapper.toDtoAcoes(aab));	
		}
		return bancoResponseDto;
	}

	private AcoesDto inserirAlterarEstrategiasPorAcao(Acoes2Dto filter, Estrategias est2, AcoesDto aaa) {
		AcoesDto aa = aaa;
//		EstrategiasPorAcao estAcao1 = new EstrategiasPorAcao();
//		estAcao1.setAcaoid(aa.getId());
//		estAcao1.setEstrategiaid(est2.getId());
//		estAcao1.setTipo(filter.getTipo());
//		List<EstrategiasPorAcao> isEstrategia = estrategiasPorAcaoService.isExitEstretegia(estAcao1);
//		if(filter.getTipo().equals(aa.getTipo())) {
//			Integer count = (aa.getLevel() == null ? 0 : aa.getLevel()) +1;
//			if(isEstrategia == null) {
//				aa.setLevel(count);
//				extracted(filter.getValorcompra(),estAcao1, 1);
//			} else {
//				if(isEstrategia.size() == 1) {
//					if(filter.getTipo().equals(isEstrategia.get(0).getTipo())) {
//						extracted(filter.getValorcompra(),isEstrategia.get(0), isEstrategia.get(0).getQuantidade());						
//					} else {
//						extracted(filter.getValorcompra(),estAcao1, 1);
//					}
//					
//					aa.setLevel(count);					
//				} else {
//					EstrategiasPorAcao estAcao2 = new EstrategiasPorAcao();
//					for (EstrategiasPorAcao estrategiasPorAcao : isEstrategia) {
//						if(filter.getTipo().equals(estrategiasPorAcao.getTipo())) {
//							estAcao2 = estrategiasPorAcao;
//						}
//					}
//					extracted(filter.getValorcompra(),estAcao2, isEstrategia.get(0).getQuantidade());
//				}
//			}
//		} else {
//			Integer count = (aa.getLevel() == null ? 0 : aa.getLevel()) +1;
//			if (isEstrategia == null) {
//				aa.setLevel(count);
//				extracted(filter.getValorcompra(),estAcao1, 1);
//			} else if(isEstrategia.size() == 1) {
//				if(filter.getTipo().equals(isEstrategia.get(0).getTipo())) {
//					extracted(filter.getValorcompra(),isEstrategia.get(0), isEstrategia.get(0).getQuantidade());						
//				} else {
//					extracted(filter.getValorcompra(),estAcao1, 1);
//				}
//				
//				aa.setLevel(count);					
//			} else {
//				EstrategiasPorAcao estAcao2 = new EstrategiasPorAcao();
//				for (EstrategiasPorAcao estrategiasPorAcao : isEstrategia) {
//					if(filter.getTipo().equals(estrategiasPorAcao.getTipo())) {
//						estAcao2 = estrategiasPorAcao;
//					}
//				}
//				extracted(filter.getValorcompra(),estAcao2, isEstrategia.get(0).getQuantidade());
//			}
//			aa.setMudouLado(1);
//		}
		
		return aa;
	}

	private EstrategiasPorAcao extracted(Double valorcompra, EstrategiasPorAcao estAcao1, Integer isEstrategia) {
//		estAcao1.setQuantidade(isEstrategia);
//		estAcao1.setStatus("P");
//		estAcao1.setDh_created_at(LocalDateTime.now());
//		estAcao1.setValorcompra(valorcompra);
		return estrategiasPorAcaoService.insert(estAcao1);
	}

	private EstrategiasPorAcao insertEstrategiasPorAcao(Acoes2Dto filter, Estrategias est2, Acoes aab) {
		EstrategiasPorAcao estAcao1 = new EstrategiasPorAcao();
		estAcao1.setAcao(aab);
		estAcao1.setEstrategia(est2);
		estAcao1.setStatus("P");
		estAcao1.setTipo(aab.getTipo());
		estAcao1.setValorcompra(filter.getValorcompra());
		return estrategiasPorAcaoService.insert(estAcao1);
	}

	private Estrategias insertEstragegia(Acoes2Dto filter) {
		Estrategias est1 = new Estrategias();
		est1.setEstrategia(filter.getEstrategia());
		Estrategias est2 =  estrategiaService.insert(est1);
		return est2;
	}

	public AcoesResponseDto compra(AcoesDto filter) {
		AcoesResponseDto bancoResponseDto = new AcoesResponseDto();
		Acoes aab = bancoRepository.save(bancoMapper.toDtoAcoes(filter));
		AcoesDto aa = bancoMapper.toDtoAcoes(aab); 
		bancoResponseDto.setBanco(new ArrayList<AcoesDto>());
		bancoResponseDto.getBanco().add(aa);
		return bancoResponseDto;
	}

	public AcoesResponseDto vendas(AcoesDto filter) {
		AcoesResponseDto bancoResponseDto = new AcoesResponseDto();
		Acoes aab = bancoRepository.save(bancoMapper.toDtoAcoes(filter));
		AcoesDto aa = bancoMapper.toDtoAcoes(aab); 
		bancoResponseDto.setBanco(new ArrayList<AcoesDto>());
		bancoResponseDto.getBanco().add(aa);
		return bancoResponseDto;
	}

	@Transactional
	public String compraVender(ArrayList<Acoes3Dto> filter) {
		String sRetorno = "";
		for (Acoes3Dto acoes3Dto : filter) {
			Optional<Acoes> aab = bancoRepository.findById(acoes3Dto.getAcaoId());
			Acoes ac = new Acoes();
			Integer totalContratos = 0;
			if (aab.isPresent()) {
//				ac = aab.get();
//				totalContratos = 0;
//				Ordens ordem = new Ordens();
//				ordem.setAcaoid(ac.getId());
//				ordem.setTipo(acoes3Dto.getTipo());
//				ordem.setContratos(acoes3Dto.getContratos());
//				ordem.setDh_created_at(LocalDateTime.now());
//				ordem.setStatus("A");
//				ordem.setLoss(acoes3Dto.getLoss());
//				ordem.setGain(acoes3Dto.getGain());
//				ordem.setAmbiente(acoes3Dto.getAmbiente());
//				if ("V".equals(acoes3Dto.getTipo())) {
//					ordem.setValorvenda(ac.getValoracaoatual() == null || ac.getValoracaoatual() == 0 ? ac.getValorsuj()
//							: ac.getValoracaoatual());
//					ordem.setDh_venda_at(LocalDateTime.now());
//					totalContratos = ac.getContratos() == null ? 0 : ac.getContratos();
//					if ("V".equals(ac.getTipo())) {
//						totalContratos = totalContratos + acoes3Dto.getContratos();
//					} else {
//						totalContratos = totalContratos - acoes3Dto.getContratos();
//					}
//					ac.setDataVenda(LocalDateTime.now());
//				} else {
//					ordem.setValorcompra(
//							ac.getValoracaoatual() == null || ac.getValoracaoatual() == 0 ? ac.getValorsuj()
//									: ac.getValoracaoatual());
//					ordem.setDh_compra_at(LocalDateTime.now());
//					totalContratos = ac.getContratos() == null ? 0 : ac.getContratos();
//					if ("C".equals(ac.getTipo())) {
//						totalContratos = totalContratos + acoes3Dto.getContratos();
//					} else {
//						totalContratos = totalContratos - acoes3Dto.getContratos();
//					}
//					ac.setDataCompra(LocalDateTime.now());
//				}
//
//				Ordens ac2 = ordensRepository.save(ordem);
//				if(totalContratos <= 0) {
//					ac.setStatus("F");
//				} else {
//					ac.setStatus("A");
//				}
//				ac.setContratos(totalContratos);
//				ac.setValor(ac.getValoracaoatual() == null || ac.getValoracaoatual() == 0 ? ac.getValorsuj()
//						: ac.getValoracaoatual());
//				Double lucroPrejAnt = ac.getLucropreju() == null  ? 0 : ac.getLucropreju();
//				ac.setLucropreju(lucroPrejAnt + calcularLucroPrej(ac,acoes3Dto,ac2.getId()));
//				Acoes aabb = bancoRepository.save(ac);

			} else {
				sRetorno = "Ação não existente.";
			}

		}
		return sRetorno;
	}
	
	@Transactional
	public String compraVender2(ArrayList<Acoes3Dto> filter) {
		String sRetorno = "";
		for (Acoes3Dto acoes3Dto : filter) {
			Optional<Acoes> aab = bancoRepository.findById(acoes3Dto.getAcaoId());
			Acoes ac = new Acoes();
			Integer totalContratos = 0;
			if (aab.isPresent()) {
				ac = aab.get();
				Ordens ordens = new Ordens(acoes3Dto,ac);
				totalContratos = 0;
				if ("V".equals(acoes3Dto.getTipo())) {
					totalContratos = acoes3Dto.getContratos() == null ? 0 : acoes3Dto.getContratos();
					if ("V".equals(ac.getTipo())) {
						totalContratos = totalContratos + acoes3Dto.getContratos();
					} else {
						totalContratos = totalContratos - acoes3Dto.getContratos();
					}
				} else {
					totalContratos = acoes3Dto.getContratos() == null ? 0 : acoes3Dto.getContratos();
					if ("C".equals(ac.getTipo())) {
						totalContratos = totalContratos + acoes3Dto.getContratos();
					} else {
						totalContratos = totalContratos - acoes3Dto.getContratos();
					}
				}
				if(totalContratos <= 0) {
					ordens.setStatus("F");
				} else {
					ordens.setStatus("D");
				}
				ordens.setContratos(totalContratos);
				ordens.setValor(ac.getValoracaoatual() == null || ac.getValoracaoatual() == 0 ? ac.getValorsuj()
						: ac.getValoracaoatual());
				Double lucroPrejAnt = ac.getLucropreju() == null  ? 0 : ac.getLucropreju();
				Ordens aabb = ordensRepository.save(ordens);
				sRetorno = "Ação cadastrada com sucesso.";
			} else {
				sRetorno = "Ação não existente.";
			}

		}
		return sRetorno;
	}

	private Double calcularLucroPrej (Acoes acoes,Acoes3Dto acoes3Dto,Integer idOrdem) {
		Double total = 0.0;
		List<Ordens> ordemMae = new ArrayList<Ordens>();
	//	List<Ordens> ordemContrario = new ArrayList<Ordens>();
		Ordens ordemContrario = new Ordens();
		Integer contratos = acoes3Dto.getContratos() == null ? 0 : acoes3Dto.getContratos();
		Integer count = 0;
		Optional<Ordens> ord =  ordensRepository.findById(idOrdem);
		ordemContrario = ord.get();
		if("C".equals(acoes.getTipo())) {
			ordemMae =  ordensRepository.findDistinctByOrdens(acoes.getId(),"C");
		//	ordemContrario =  ordensRepository.findDistinctByOrdens(acoes.getId(),"V");
			if("V".equals(acoes3Dto.getTipo())) {
				for (Ordens ordens : ordemMae) {
//					count = ordens.getContratos() - ordens.getContratosCorrentes();
//					if (count > 0) {
//						if(contratos == count) {
//							//TODO ZERAR ORDEM MUDAR STATUS PRA F E calcular Lucro/Prej
//							ordens.setStatus("F");
//							ordens.setContratosCorrentes(contratos);
//							ordens.setValorvenda(ordens.getValorvenda());
//							ordens.setDh_venda_at(ordens.getDh_venda_at());
//							total = contratos * ordens.getValorvenda();
//							ordensRepository.save(ordens);
//							return total;
//						}
//						//TODO CALCULAR PARCIAL
//						//else if(contratos == count) {}
//					}
				}
				count = 0;
			}
		} else {
			ordemMae =  ordensRepository.findDistinctByOrdens(acoes.getId(),"V");
			//ordemContrario =  ordensRepository.findDistinctByOrdens(acoes.getId(),"C");
			if("C".equals(acoes3Dto.getTipo())) {
				for (Ordens ordens : ordemMae) {
//					count = ordens.getContratos() - (ordens.getContratosCorrentes() == null ? 0 : ordens.getContratosCorrentes()) ;
//					if (count > 0) {
//						if(contratos == count) {
//							//TODO ZERAR ORDEM MUDAR STATUS PRA F E calcular Lucro/Prej
//							ordemContrario.setStatus("F");
//							ordemContrario.setContratosCorrentes(contratos);
//							total = contratos * ordemContrario.getValorvenda();
//							ordensRepository.save(ordemContrario);
//							return total;
//						} else if (contratos < count) {
//							ordemContrario.setStatus("F");
//							ordemContrario.setContratosCorrentes(contratos);
//							total = contratos * (ordemContrario.getValorvenda() == null ? 0 : ordemContrario.getValorvenda());
//							ordemContrario.setValorvenda(ordens.getValorvenda());
//							ordemContrario.setDh_venda_at(ordens.getDh_venda_at());
//							ordensRepository.save(ordemContrario);
//							return total;
//						}
						//TODO CALCULAR PARCIAL
						//else if(contratos == count) {}
					//}
				}
				count = 0;
			}
		}
		
		return total;
	}

	public ArrayList<AcoesResponseDto2> buscaAcoesPaginadosSearchFiltro(AcaoFilterSearchRequestDto filter, Pageable pageable) {
		try{
            ArrayList<AcoesResponseDto2> boletos = filtraBoletosSearch(filter, null);
            return boletos;
        }catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Erro ao buscar açoes");
        }
	}
	
	public ArrayList<AcoesResponseDto2> buscaOrdensPaginadosSearchFiltro(AcaoFilterSearchRequestDto filter, Pageable pageable) {
		try{
            ArrayList<AcoesResponseDto2> boletos = filtraOrdensSearch(filter, null);
            return boletos;
        }catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Erro ao buscar açoes");
        }
	}
	
	public ArrayList<AcoesResponseDto4> buscaAcoesPaginadosSearchFiltroErros(AcaoFilterSearchRequestDto filter, Pageable pageable) {
		try{
            ArrayList<AcoesResponseDto4> boletos = filtraBoletosSearchErros(filter, null);
            return boletos;
        }catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Erro ao buscar açoes");
        }
	}
	
	public ArrayList<AcoesResponseDto2> filtraBoletosSearch(AcaoFilterSearchRequestDto filter, Pageable pageable){
       // List<AcoesDto> boletolist = bancoMapper.toDtoListAcoes(acoesCustomSearchRepository.findByRequest(filter,pageable));
        List<AcoesDto> boletolist = bancoMapper.toDtoListAcoes(acoesCustomSearchRepository.findByRequest(filter,pageable));
        return getFiltraBoletos(boletolist);
    }
	
	public ArrayList<AcoesResponseDto2> filtraOrdensSearch(AcaoFilterSearchRequestDto filter, Pageable pageable){
	       // List<AcoesDto> boletolist = bancoMapper.toDtoListAcoes(acoesCustomSearchRepository.findByRequest(filter,pageable));
			List<Ordens> boletolist = acoesCustomSearchRepository.findByRequestOrdens(filter,pageable);
			List<AcoesDto> ordensList = new ArrayList<>();
			for (Ordens ordens : boletolist) {
				ordensList.add(new AcoesDto(ordens));
			}
	        return getFiltraOrdens(ordensList);
	    }
	
	public ArrayList<AcoesResponseDto4> filtraBoletosSearchErros(AcaoFilterSearchRequestDto filter, Pageable pageable){
        List<Acoes5Dto> boletolist = acoesCustomSearchRepository.findByRequestErros(filter,pageable);
        return getFiltraBoletosErros(boletolist);
    }
	
	private ArrayList<AcoesResponseDto2> getFiltraBoletos(List<AcoesDto> boletolist) {
		ArrayList<AcoesResponseDto2> bancoResponseDto = new ArrayList<AcoesResponseDto2>();
		for (Acoes2Dto acoesResponseDto : bancoMapper.toDtoListAcoesDto(boletolist)) {
			AcoesResponseDto2 response = new AcoesResponseDto2();
			acoesResponseDto.setDh_created_at(acoesResponseDto.getAudit().getDataCreated());
			acoesResponseDto.setDh_updated_at(acoesResponseDto.getAudit().getDataUpdated());
			acoesResponseDto.setEstrategia(estrategiaService.getEstrategiasString(acoesResponseDto.getId()));
			acoesResponseDto.setQtdEstrategia((estrategiaService.getEstrategias(acoesResponseDto.getId())).size());
			response.setBanco(acoesResponseDto);
			bancoResponseDto.add(response);
		}
		return bancoResponseDto;
	}
	
	private ArrayList<AcoesResponseDto2> getFiltraOrdens(List<AcoesDto> boletolist) {
		ArrayList<AcoesResponseDto2> bancoResponseDto = new ArrayList<AcoesResponseDto2>();
		for (Acoes2Dto acoesResponseDto : bancoMapper.toDtoListAcoesDto(boletolist)) {
			AcoesResponseDto2 response = new AcoesResponseDto2();
			acoesResponseDto.setDh_created_at(acoesResponseDto.getAudit().getDataCreated());
			acoesResponseDto.setDh_updated_at(acoesResponseDto.getAudit().getDataUpdated());
			acoesResponseDto.setEstrategia(estrategiaService.getEstrategiasString(acoesResponseDto.getAcaoId()));
			acoesResponseDto.setError(errosService.getErrosAcaoIdByString(acoesResponseDto.getId()));
			acoesResponseDto.setQtdBreakeven((breakevenService.findByAcaoId(acoesResponseDto.getId())).size());
			acoesResponseDto.setQtdEstrategia((estrategiaService.getEstrategias(acoesResponseDto.getAcaoId())).size());
			response.setBanco(acoesResponseDto);
			bancoResponseDto.add(response);
		}
		return bancoResponseDto;
	}
	
	private ArrayList<AcoesResponseDto4> getFiltraBoletosErros(List<Acoes5Dto> boletolist) {
		ArrayList<AcoesResponseDto4> bancoResponseDto = new ArrayList<AcoesResponseDto4>();
		for (Acoes5Dto acoesResponseDto : bancoMapper.toDtoListAcoes5(boletolist)) {
			AcoesResponseDto4 response = new AcoesResponseDto4();
			acoesResponseDto.setEstrategia(estrategiaService.getEstrategiasString(acoesResponseDto.getId()));
			response.setErros(acoesResponseDto);
			bancoResponseDto.add(response);
		}
		return bancoResponseDto;
	}
	
	public Acoes3Dto getAcaoById(Integer id) {
		Acoes3Dto acao = new Acoes3Dto();
		
		Optional<Acoes> op = bancoRepository.findById(id);
		if(op.isPresent()) {
			acao = convert(op.get());
		}
		return acao;
	}
	
	private Acoes3Dto convert(Acoes dto) {
		Acoes3Dto acoes = new Acoes3Dto();
//		acoes.setAcaoId(dto.getId());
//		acoes.setTipo(dto.getTipo());;
//		acoes.setAmbiente(dto.getAmbiente());;
//		acoes.setData(LocalDateTime.now());
//		acoes.setValoracao(dto.getValoracaoatual());
//		acoes.setContratos(dto.getContratos());
//		acoes.setLoss(0.0);
//		acoes.setGain(0.0);
		return acoes;
	}

	public ArrayList<AcoesResponseDto3> buscaValorAtualAcao(String id) {


		ArrayList<AcoesResponseDto3> resp = new ArrayList<>();
		try {
			HttpEntity<?> httpEntity = new HttpEntity<>(createJSONHeaderAcao());

			String params = "" + id;

			try {
				ResponseEntity<AcaoRetornoDto5> response = new RestTemplate().exchange(getDefaultQueryParameters(params),
						HttpMethod.GET, httpEntity, AcaoRetornoDto5.class);
				
				for (AcaoRetornoDto4 string : response.getBody().getResults()) {
					if(string.getRegularMarketPrice() != null) {
						resp.add(new AcoesResponseDto3(string.getRegularMarketPrice()));
					}
						
				}

				System.out.println("teste");
			} catch (HttpClientErrorException ee) {
				System.out.println("teste");
			} catch (IllegalArgumentException eed) {
				System.out.println("teste");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}	
		return resp;
	}
	
	private HttpHeaders createJSONHeaderAcao() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36");

        return headers;
    }
	 private String getDefaultQueryParameters(String query) {
	        return "https://brapi.dev/api/quote/" + query;
	    }

	@Transactional
	public String compraVender3(ArrayList<Acoes4Dto> filter) {
		try {
			for (Acoes4Dto acoes4Dto : filter) {
				bancoRepository.deleteById(acoes4Dto.getAcaoId());
			}
			return "Ações deletadas com sucesso!";
		} catch (Exception e) {
			System.out.println(e);
			return "Erro ao deletar Ações";
		}
	}
	
	@Transactional
	public String closeAcao(ArrayList<Acoes4Dto> filter) {
		try {
			for (Acoes4Dto acoes4Dto : filter) {
				Optional<Acoes> op = bancoRepository.findById(acoes4Dto.getAcaoId());
				if(op.isPresent()) {
					Acoes acoes = op.get();
					acoes.setStatus("F");
					//acoes.setDh_updated_at(LocalDateTime.now());
					bancoRepository.save(acoes);
				}
				
			}
			return "Ações inseridas com sucesso!";
		} catch (Exception e) {
			System.out.println(e);
			return "Erro ao fechar Ações = "+ e.getMessage();
		}
	}
	
	@Transactional
	public String efetivarClose(Acoes6Dto filter) {
		try {

				Optional<Ordens> op = ordensRepository.findById(filter.getAcaoId());
				if(op.isPresent()) {
					Ordens acoes = op.get();
					if(filter.getError() != null && "".equals(filter.getError().trim())) {
						acoes.setStatus("C");						
					}else {
						acoes.setStatus("E");
						Erros erro = new Erros();
						erro.setOrdem(acoes);
						erro.setErro(filter.getError());
						errosService.insert(erro);
					}
				//	acoes.setDh_updated_at(LocalDateTime.now());
					ordensRepository.save(acoes);
				}
				
			
			return "Ações inseridas com sucesso!";
		} catch (Exception e) {
			System.out.println(e);
			return "Erro ao fechar Ações = "+ e.getMessage();
		}
	}
}
