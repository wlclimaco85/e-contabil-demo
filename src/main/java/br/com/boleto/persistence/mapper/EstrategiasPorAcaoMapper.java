package br.com.boleto.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.boleto.persistence.dtos.AcoesDto;
import br.com.boleto.persistence.dtos.EstrategiasPorAcaoDto;
import br.com.boleto.persistence.entity.EstrategiasPorAcao;

@Mapper(componentModel = "spring")
public interface EstrategiasPorAcaoMapper {
    AcoesDto toDtoEstrategiasPorAcao(EstrategiasPorAcao entity);

    ArrayList<EstrategiasPorAcaoDto> toDtoListEstrategiasPorAcao(List<EstrategiasPorAcao> list);
}
