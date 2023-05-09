package br.com.boleto.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.boleto.persistence.dtos.Acoes2Dto;
import br.com.boleto.persistence.dtos.Acoes5Dto;
import br.com.boleto.persistence.dtos.AcoesDto;
import br.com.boleto.persistence.entity.Acoes;

@Mapper(componentModel = "spring")
public interface AcoesMapper {
    AcoesDto toDtoAcoes(Acoes entity);
    Acoes toDtoAcoes(AcoesDto entity);
    
    ArrayList<AcoesDto> toDtoListAcoes(List<Acoes> list);
    ArrayList<Acoes2Dto> toDtoListAcoes2(List<Acoes> list);
    ArrayList<Acoes2Dto> toDtoListAcoesDto(List<AcoesDto> list);
    
    ArrayList<Acoes5Dto> toDtoListAcoes5(List<Acoes5Dto> list);
}
