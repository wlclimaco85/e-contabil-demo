package br.com.boleto.service.implementation;

import br.com.boleto.persistence.dtos.ContaCredenciamentoDto;
import br.com.boleto.persistence.entity.ContaNaoCredenciada;
import br.com.boleto.persistence.mapper.ContaNaoCredenciadaMapper;
import br.com.boleto.persistence.repository.BoletoRepository;
import br.com.boleto.persistence.repository.BoletoStatusFinalRepository;
import br.com.boleto.persistence.repository.ContaNaoCredenciadaRepository;
import static java.util.Objects.isNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.flywaydb.core.internal.util.FileCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.enums.TipoBaseEnum;
import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.ContaDto;
import br.com.boleto.persistence.dtos.ContaPesquisaDto;
import br.com.boleto.persistence.dtos.ContaResponseDto;
import br.com.boleto.persistence.dtos.ContaSearchRequestDto;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.ResponseDto;
import br.com.boleto.persistence.entity.Certificado;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.persistence.mapper.ContaMapper;
import br.com.boleto.persistence.repository.BoletoCustomSearchRepository;
import br.com.boleto.persistence.repository.ContaRepository;
import br.com.boleto.service.AuthenticationFactory;
import br.com.boleto.util.Constantes;
import br.com.boleto.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContaService {
    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaNaoCredenciadaRepository contaNaoCredenciadaRepository;

    @Autowired
    private BoletoService boletoService;
    
    @Autowired
    private ParceiroService parceiroService;

    @Autowired
    private AuthenticationFactory authenticationFactory;

    @Autowired
    private ContaMapper contaMapper;

    @Autowired
    private ContaNaoCredenciadaMapper contaNaoCredenciadaMapper;
    
    @Autowired
    private HttpServletResponse response;
    
    @Value("${path.api.itau.pem}")
    private String localChavesPEM;
    
    @Autowired
    BoletoCustomSearchRepository boletoCustomSearchRepository;

    @Autowired
    BoletoRepository boletoRepository;

    @Autowired
    BoletoStatusFinalRepository boletoStatusFinalRepository;

    public ArrayList<ContaResponseDto> getContas() {
    	ArrayList<ContaResponseDto> resp = new ArrayList<ContaResponseDto>();
        List<Conta> boletolist = contaRepository.findAll();
        for (Conta conta : boletolist) {
			resp.add(new ContaResponseDto(conta));
		}
		return resp;
    }
    public ResponseDto gerenciarCredenciamentoConta(ContaCredenciamentoDto contaDto) {
        ResponseDto response;

        if (Boolean.TRUE.equals(contaDto.getDescredenciar()) && "N".equals(contaDto.getStatusapi())) {
            response = descredenciarConta(contaDto);
        } else {
            response = credenciarConta(contaDto);
            validaCredencial(contaDto,response.getId());
        }
    	return response;
    }

    @Transactional
    private ResponseDto descredenciarConta(ContaCredenciamentoDto contaDto) {
        Optional<Conta> contaOpt = contaRepository.buscarContaIdParceiroCodCtaRegistroBase(contaDto.getCodcta(),contaDto.getIdparceiro(),contaDto.getTipobase());
        if (contaOpt.isPresent()) {
            contaRepository.descredenciaConta(DateUtil.getDataAtual(), contaOpt.get().getId());
            deleteContasCredenciadas(contaOpt.get());
            return new ResponseDto(contaOpt.get().getId(),"A conta foi descredenciada e a integração com a API foi desativada.");
        }
        throw new NotFoundException("Não foi encontrada a conta para descredenciamento.");
    }

    @Transactional
     private ResponseDto credenciarConta(ContaCredenciamentoDto contaDto) {
        Integer idConta = null;
        Optional<Conta> contaOpt = Optional.empty();
        try {
            contaOpt = contaRepository.buscarContaIdParceiroCodCtaRegistroBase(contaDto.getCodcta(),contaDto.getIdparceiro(),contaDto.getTipobase());
            if (contaOpt.isEmpty()) {
                idConta = criarContaCredenciada(contaDto);
            } else {
                idConta = alterarContaCredenciada(contaDto, contaOpt.get());
            }
            return new ResponseDto(idConta, String.format("As chaves %s já foram testadas e a integração com a API foi concluída.", contaOpt.isEmpty() ? "inseridas" : "alteradas" ));
        } catch (Exception e){
            log.error("Tivemos um problema ao credenciar sua conta. Por favor, tente novamente. - Id: {} [Método: {}]", contaOpt.isEmpty() ? "criaCredenciamento" : "alteraCredenciamento", isNull(idConta) ? "Nulo" : idConta.toString());
            log.error(e.getMessage(), e);
            throw new NotFoundException("Tivemos um problema ao criar sua conta. Por favor, tente novamente.");
        }
    }
    private Integer alterarContaCredenciada(ContaCredenciamentoDto contaCredDto, Conta conta) {
        if("S".equals(conta.getStatusapi())) {
            contaCredDto.setClientid(conta.getClientid());
            contaCredDto.setClientsecret(conta.getClientsecret());
            contaCredDto.setStatusapi(conta.getStatusapi());
        }
        contaMapper.mergeContaCredenciamentoDtoToConta(contaCredDto, conta);
        Conta contaResult = contaRepository.save(conta);
        return contaResult.getId();
    }

    private Integer criarContaCredenciada(ContaCredenciamentoDto contaCredenciamentoDto) {
        Conta conta;
        Optional<ContaNaoCredenciada> contaNaoCredenciadaOpt = contaNaoCredenciadaRepository.
                findByCodctaAndParceiroIdAndRegistrobase(contaCredenciamentoDto.getCodcta(), contaCredenciamentoDto.getIdparceiro(), contaCredenciamentoDto.getTipobase());

        if (contaNaoCredenciadaOpt.isEmpty()) {
            conta = contaMapper.toEntityContaCredenciamentoDto(contaCredenciamentoDto);
        } else {
            ContaDto contaNaoCredenciadaDto = contaNaoCredenciadaMapper.toDtoContaNaoCredenciada(contaNaoCredenciadaOpt.get());
            conta = contaMapper.toEntityContaDto(contaNaoCredenciadaDto);
            conta.setId(null);
            contaMapper.mergeContaCredenciamentoDtoToConta(contaCredenciamentoDto, conta);
            contaNaoCredenciadaRepository.deleteByCodctaAndParceiroIdAndRegistrobase(contaCredenciamentoDto.getCodcta(), contaCredenciamentoDto.getIdparceiro(), contaCredenciamentoDto.getTipobase());
        }
        parceiroService.salva(conta.getParceiro());
        Conta contaResult = contaRepository.save(conta);

        if (TipoBaseEnum.PRODUCAO.getTipoBase().equals(contaResult.getRegistrobase()) || TipoBaseEnum.NAO_REGISTRADA.getTipoBase().equals(contaResult.getRegistrobase())) {
            atualizaBoletos(contaResult);
        }
        return contaResult.getId();
    }


    public void validaCredencial(ContaCredenciamentoDto contaDto,Integer conta_id){
        if (!contaDto.getDescredenciar() && (!"******************************".contains(contaDto.getClientid()) || !"******************************".contains(contaDto.getClientsecret()))) {
            BancoEnum bancoEnum = BancoEnum.valueById(contaDto.getCodbco());
            LoginDto loginDto = new LoginDto(contaDto.getClientid(), contaDto.getClientsecret(), bancoEnum, conta_id, contaDto.getToken(), contaDto.getChavesessao());
            Certificado certificado = null;
            if (Objects.equals(bancoEnum.getId(), BancoEnum.ITAU.getId())) {
                certificado = authenticationFactory.getCertificado(bancoEnum).certificado(loginDto);
            }else {
                authenticationFactory.getAuthentication(bancoEnum).authentication(loginDto);
            }

            Optional<Conta> conta = contaRepository.findById(conta_id);
            if(conta.isPresent()) {
                if(certificado != null) {
                    contaRepository.alteraClientIdClientSecret(certificado.getClientid(),certificado.getClientsecret(),conta_id);
                }
            }
            contaRepository.alteraStatusConta("S",conta_id);
        }
    }


   @Transactional
  public ResponseDto sincronizarDadosConta(ContaDto contaDto) {
      Integer idConta = null;
      try {
          Optional<Conta> contaOpt = contaRepository.buscarContaIdParceiroCodCtaRegistroBase(contaDto.getCodcta(), contaDto.getIdparceiro(), contaDto.getTipobase());
          if (contaOpt.isPresent()) {
              idConta = sincronizarContasCredenciadas(contaDto, contaOpt.get());
          } else {
              idConta = sincronizarContasNaoCredenciadas(contaDto);
          }
          return new ResponseDto(idConta, "Conta incluída/alterada com sucesso!");
      } catch (Exception e) {
          log.error("Tivemos um problema ao criar/alterar a conta. Por favor, tente novamente. - Id: {} [Método: sincronizaDadosConta]", isNull(idConta) ? "Nulo" : idConta.toString());
          log.error(e.getMessage(), e);
          throw new NotFoundException("Tivemos um problema ao criar/alterar sua conta. Por favor, tente novamente.");
      }
  }


    private Integer sincronizarContasCredenciadas(ContaDto contaDto, Conta conta) {
            contaMapper.mergeDtoToContaSincronizada(contaDto, conta);
            Conta contaResult = contaRepository.save(conta);
            return contaResult.getId();
    }

    private Integer sincronizarContasNaoCredenciadas(ContaDto contaDto) {
        Optional<ContaNaoCredenciada> contaNaoCredenciadaOpt = contaNaoCredenciadaRepository.findByCodctaAndParceiroIdAndRegistrobase(contaDto.getCodcta(), contaDto.getIdparceiro(), contaDto.getTipobase());
        ContaNaoCredenciada contaNaoCredenciada = contaNaoCredenciadaOpt.orElseGet(ContaNaoCredenciada::new);
        contaNaoCredenciadaMapper.mergeDtoToContaNaoCredenciada(contaDto, contaNaoCredenciada);
        if (isNull(contaNaoCredenciada.getId())) {
            parceiroService.salva(contaNaoCredenciada.getParceiro());
        }
        ContaNaoCredenciada contaNaoCredenciadaResult = contaNaoCredenciadaRepository.save(contaNaoCredenciada);
        return contaNaoCredenciadaResult.getId();
    }

    public ContaPesquisaDto pesquisaContaPorId(Integer contaId) {
        Optional<Conta> conta = contaRepository.findById(contaId);

        if (conta.isPresent()) {
            ContaPesquisaDto contaResponse = contaMapper.toDtoContaPesquisa(conta.get());
            return contaResponse;
        } 
        log.info("Conta não existe na base de dados. - Id: {} [Método: pesquisar]", isNull(contaId) ? "Nulo" : contaId.toString());
        throw new NotFoundException("Conta não existe na base de dados.");   
    }
    
    public InputStream downloadFile() {
    	try {
    		File file = ResourceUtils.getFile(localChavesPEM +"/public.pem");
    		if (file.exists()) {
    			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
    			if (mimeType == null) {
    				mimeType = "application/octet-stream";
    			}
    			response.setContentType(mimeType);
    			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
    			response.setContentLength((int) file.length());
    			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
    			FileCopyUtils.copy(inputStream, response.getOutputStream());
    		}
    	}catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NotFoundException("Arquivo não encontrado no Servidor.");
        }
    	
		return null;
    }

    public ArrayList<ContaDto> buscaContaBaseProducao() {
        ArrayList<ContaDto> contasDto = new ArrayList<>();
        List<Conta> contas = contaRepository.findByRegistrobaseAndStatusapi(TipoBaseEnum.PRODUCAO.getTipoBase(), "S");

        if (!contas.isEmpty()) {
            for (Conta conta : contas) {
                contasDto.add(new ContaDto(conta));
            }
        }
        return contasDto;
    }

    private void atualizaBoletos(Conta conta) {
        new Thread() {
            @Override
            public void run() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String dataInicial = LocalDate.now().minusMonths(18).format(formatter);
                String dataFinal = LocalDate.now().format(formatter);
                boletoService.atualizaBoletos(dataInicial, dataFinal, conta.getId(), conta.getCodage(), "A");

                dataInicial = LocalDate.now().minusDays(7).format(formatter);
                boletoService.atualizaBoletos(dataInicial, dataFinal, conta.getId(), conta.getCodage(), "B");
            }
        }.start();
    }

    public boolean estaInativa(ContaDto contaDto) {
        if (contaDto != null && "N".equals(contaDto.getStatusapi()) ||
                ((isNull(contaDto.getAudit().getDataCreated()) && isNull(contaDto.getAudit().getDataUpdated())) ||
                        (!isNull(contaDto.getAudit().getDataCreated()) && isNull(contaDto.getAudit().getDataUpdated()) && contaDto.getAudit().getDataCreated().plusDays(Constantes.SETE_DIAS).isBefore(DateUtil.getDataAtual())) ||
                        (!isNull(contaDto.getAudit().getDataUpdated()) && contaDto.getAudit().getDataUpdated().plusDays(Constantes.SETE_DIAS).isBefore(DateUtil.getDataAtual()))) &&
                        (TipoBaseEnum.TESTE.getTipoBase().equals(contaDto.getTipobase()) || TipoBaseEnum.TREINAMENTO.getTipoBase().equals(contaDto.getTipobase()))) {
            List<BoletoDto> boletoDtolist = boletoService.buscaPorConta(contaDto.getId());
            if (!boletoDtolist.isEmpty()) {
                if (boletoDtolist.size() > 1) {
                    LocalDateTime ultimaAtualizacao = boletoService.buscaUltimaDataAtualizacaoPorConta(contaDto.getId());
                    if (ultimaAtualizacao != null && ultimaAtualizacao.plusDays(Constantes.SETE_DIAS).isBefore(DateUtil.getDataAtual())) {
                        return true;
                    }
                } else if (((boletoDtolist.get(0).getAudit().getDataCreated() != null && boletoDtolist.get(0).getAudit().getDataUpdated() == null && boletoDtolist.get(0).getAudit().getDataCreated().plusDays(Constantes.SETE_DIAS).isBefore(DateUtil.getDataAtual())) ||
                        (boletoDtolist.get(0).getAudit().getDataUpdated() != null && boletoDtolist.get(0).getAudit().getDataUpdated().plusDays(Constantes.SETE_DIAS).isBefore(DateUtil.getDataAtual())))) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Conta> findByContaRequest(ContaSearchRequestDto filter, Pageable pageable){
		return (ArrayList<Conta>) boletoCustomSearchRepository.findByContaRequest(filter);
    }

    @Transactional
    public ResponseDto deleteConta(ContaDto contaDto) {
        try {
            Optional<Conta> contaOpt = contaRepository.buscarContaIdParceiroCodCtaRegistroBase(contaDto.getCodcta(), contaDto.getIdparceiro(), contaDto.getTipobase());
            if (contaOpt.isPresent()) {
                contaRepository.descredenciaConta(DateUtil.getDataAtual(), contaOpt.get().getId());
                deleteContasCredenciadas(contaOpt.get());
            } else {
                contaNaoCredenciadaRepository.deleteByCodctaAndParceiroIdAndRegistrobase(contaDto.getCodcta(), contaDto.getIdparceiro(), contaDto.getTipobase());
            }
            return new ResponseDto(contaDto.getId(), "Conta excluída/alterada com sucesso.");
        } catch (Exception e){
            log.error("Tivemos um problema ao excluir sua conta. Por favor, tente novamente. - Id: {} [Método: deleteContasNaoCredenciadas]", isNull(contaDto.getId()) ? "Nulo" : contaDto.getId().toString());
            log.error(e.getMessage(), e);
            throw new NotFoundException("Tivemos um problema ao excluir sua conta. Por favor, tente novamente.");
        }
    }

    @Transactional
    public void deleteContasCredenciadas(Conta conta) {
        try {
            boolean hasBoletos = boletoRepository.existsByContaId(conta.getId());
            boolean hasBoletosStatusFinais = boletoStatusFinalRepository.existsByContaId(conta.getId());

            if (!hasBoletos && !hasBoletosStatusFinais) {
                contaRepository.delete(conta);
                ContaDto contaDto = contaMapper.toDtoConta(conta);
                ContaNaoCredenciada contaNaoCredenciada = contaNaoCredenciadaMapper.toEntityContaDto(contaDto);
                contaNaoCredenciada.setId(null);
                contaNaoCredenciada.setStatusapi("N");
                contaNaoCredenciada.setDataDesativacao(DateUtil.getDataAtual());
                contaNaoCredenciadaRepository.deleteByCodctaAndParceiroIdAndRegistrobase(contaDto.getCodcta(), contaDto.getIdparceiro(), contaDto.getTipobase());
                contaNaoCredenciadaRepository.save(contaNaoCredenciada);
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("Tivemos um problema ao excluir uma conta credenciada. Por favor, tente novamente. - Id: {} [Método: deleteContasCredenciadas]", isNull(conta.getId()) ? "Nulo" : conta.getId().toString());
            log.error(e.getMessage(), e);
        }
    }

}
