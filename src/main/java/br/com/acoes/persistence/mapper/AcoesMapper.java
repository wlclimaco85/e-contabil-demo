package br.com.acoes.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.acoes.persistence.dtos.Acoes2Dto;
import br.com.acoes.persistence.dtos.Acoes5Dto;
import br.com.acoes.persistence.dtos.AcoesDto;
import br.com.acoes.persistence.entity.Acoes;

@Mapper(componentModel = "spring")
public interface AcoesMapper {
    AcoesDto toDtoAcoes(Acoes entity);
    Acoes toDtoAcoes(AcoesDto entity);
    
    ArrayList<AcoesDto> toDtoListAcoes(List<Acoes> list);
    ArrayList<Acoes2Dto> toDtoListAcoes2(List<Acoes> list);
    ArrayList<Acoes2Dto> toDtoListAcoesDto(List<AcoesDto> list);
    
    ArrayList<Acoes5Dto> toDtoListAcoes5(List<Acoes5Dto> list);
}
