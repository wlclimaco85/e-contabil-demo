package br.com.acoes.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.acoes.persistence.dtos.OrdensDto;
import br.com.acoes.persistence.entity.Ordens;

@Mapper(componentModel = "spring")
public interface OrdensMapper {
    OrdensDto toDtoOrdens(Ordens entity);

    ArrayList<OrdensDto> toDtoListOrdens(List<Ordens> list);
}
