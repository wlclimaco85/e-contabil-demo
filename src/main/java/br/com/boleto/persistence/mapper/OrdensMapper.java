package br.com.boleto.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.boleto.persistence.dtos.AcoesDto;
import br.com.boleto.persistence.dtos.OrdensDto;
import br.com.boleto.persistence.entity.Ordens;

@Mapper(componentModel = "spring")
public interface OrdensMapper {
    AcoesDto toDtoOrdens(Ordens entity);

    ArrayList<OrdensDto> toDtoListOrdens(List<Ordens> list);
}
