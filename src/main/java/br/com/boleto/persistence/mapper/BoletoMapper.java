package br.com.boleto.persistence.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.persistence.dtos.BoletoPaginadosDto;
import br.com.boleto.persistence.entity.Boleto;
import br.com.boleto.util.DateUtil;


@Mapper(componentModel = "spring",imports = {DateUtil.class, StatusBoletoEnum.class})
public interface BoletoMapper {
    @Mapping(target = "status", expression = "java(entity.getStatus().getStatus())")
    @Mapping(target = "descricaoStatus", expression = "java(entity.getStatus().getDescricao())")
    @Mapping(target = "idapibanco", source = "entity.conta.id")
    @Mapping(target = "dataVencimento", expression = "java(DateUtil.convertTimeStampToString(entity.getDataVencimento()))")
    @Mapping(target = "dataEmissao", expression = "java(DateUtil.convertTimeStampToString(entity.getDataEmissao()))")
    BoletoPaginadosDto toEntityBoletoPaginadosDto(Boleto entity);

    List<BoletoPaginadosDto> toListEntityListBoletoPaginadosDto(List<Boleto> entitys);
}
