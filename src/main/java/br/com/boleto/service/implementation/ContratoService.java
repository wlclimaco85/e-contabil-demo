package br.com.boleto.service.implementation;

import static java.util.Objects.isNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.ContratoDto;
import br.com.boleto.persistence.dtos.ResponseDto;
import br.com.boleto.persistence.dtos.TermoDto;
import br.com.boleto.persistence.entity.Contrato;
import br.com.boleto.persistence.entity.Servico;
import br.com.boleto.persistence.entity.Termo;
import br.com.boleto.persistence.mapper.ContratoMapper;
import br.com.boleto.persistence.mapper.TermoMapper;
import br.com.boleto.persistence.repository.ContratoRepository;
import br.com.boleto.persistence.repository.ServicoRepository;
import br.com.boleto.persistence.repository.TermoRepository;
import br.com.boleto.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContratoService {
	
	@Autowired
    private ContratoRepository contratoRepository;
	
	@Autowired
    private ServicoRepository servicoRepository;
	
	@Autowired
	private TermoRepository termoRepository;
	
    @Autowired
    private ContratoMapper contratoMapper;
    
    @Autowired
    private TermoMapper termoMapper;
	
    public Double getPreco(Integer contaId, Integer idparceiro, Integer codserv, String usuContratante, Integer versaoAceite) {
        try {    	
        	ContratoDto contratoDto = new ContratoDto(contaId, idparceiro, codserv, usuContratante, versaoAceite);
        	Contrato contrato = contratoMapper.toEntityContratoDto(contratoDto);
        	Optional<Contrato> contratoOpt = contratoRepository.findByServicoAndParceiroAndConta(contrato.getServico(), contrato.getParceiro(), contrato.getConta());
        	if (contratoOpt.isPresent()) {
        		return contratoOpt.get().getValorContratado();
			}
			Optional<Servico> servicoOpt = servicoRepository.findByCodserv(codserv);
			if (servicoOpt.isPresent()) {
				return servicoOpt.get().getValor();
			}
           return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NotFoundException("Erro ao buscar preço.");
        }
    }
	
	@Transactional
	public ResponseDto salvaContrato(ContratoDto contratoDto) {
		ResponseDto response = new ResponseDto();
		try {
			Contrato contrato = contratoMapper.toEntityContratoDto(contratoDto);
			Optional<Contrato> contratoOpt = contratoRepository.findByServicoAndParceiroAndConta(contrato.getServico(), contrato.getParceiro(), contrato.getConta());
			
			if (contratoOpt.isPresent()) {
				contrato.setCodcontrato(contratoOpt.get().getCodcontrato());
				if (!isNull(contratoOpt.get().getAudit())) {
					contrato.setAudit(contratoOpt.get().getAudit());
					contrato.getAudit().setDataUpdated(DateUtil.getDataAtual());
				}
			}
			
			contrato.setValorContratado(getPreco(contratoDto.getIdapibanco(), contratoDto.getIdparceiro(), contratoDto.getCodserv(), contratoDto.getUsuContratante(), contratoDto.getVersaoAceite()));
			contrato.setAtivo("S");
        	contratoRepository.save(contrato);
        	response.setMessage("Contrato salvo com sucesso.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
            throw new NotFoundException("Erro ao salvar contrato.");
		}
		return response;
	}
	
	public TermoDto getTermo(Integer idparceiro, Integer codserv, Integer contaId) {
		ContratoDto contratoDto = new ContratoDto();
		contratoDto.setIdparceiro(contaId);
		contratoDto.setCodserv(codserv);
		contratoDto.setIdapibanco(contaId);
		
		TermoDto termoDto = new TermoDto();
		try {
			Optional<Termo> termo = termoRepository.findAtual();
			Contrato contrato = contratoMapper.toEntityContratoDto(contratoDto);
        	Optional<Contrato> contratoOpt = contratoRepository.findByServicoAndParceiroAndContaAndAtivo(contrato.getServico(), contrato.getParceiro(), contrato.getConta(), "S");
			
			if (contratoOpt.isPresent()) {
				Optional<Termo> termoContrato = termoRepository.findById(contratoOpt.get().getTermo().getVersao());
				if (termoContrato.isPresent()) {
					termoDto = termoMapper.toDtoTermo(termoContrato.get());
					if(termo.isPresent() && termoContrato.get().getVersao() < termo.get().getVersao()) {
						termoDto.setAtualizacao(true);
					}
					return termoDto;
				}
			}
			if (termo.isPresent()) {
				return termoMapper.toDtoTermo(termo.get());
			}
			return termoDto;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
            throw new NotFoundException("Erro ao pagar o termo.");
		}
	}
}
