package br.com.boleto.persistence.mapper;

import br.com.boleto.persistence.dtos.ContaDto;
import br.com.boleto.persistence.entity.ContaNaoCredenciada;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContaNaoCredenciadaMapper {

    @Mapping(target = "parceiro.id", source = "idparceiro")
    @Mapping(target = "parceiro.nome", source = "nomeparc")
    @Mapping(target = "registrobase", source = "tipobase")
    @Mapping(target = "banco.id", source = "codbco")
    @Mapping(target = "indicadorPix", source = "indicadorpix")
    ContaNaoCredenciada toEntityContaDto(ContaDto dto);

    @Mapping(target = "parceiro.id", source = "idparceiro")
    @Mapping(target = "parceiro.nome", source = "nomeparc")
    @Mapping(target = "registrobase", source = "tipobase")
    @Mapping(target = "banco.id", source = "codbco")
    @Mapping(target = "indicadorPix", source = "indicadorpix")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientid", ignore = true)
    @Mapping(target = "clientsecret", ignore = true)
    ContaNaoCredenciada mergeDtoToContaNaoCredenciada(ContaDto contaDto, @MappingTarget ContaNaoCredenciada conta);

    @Mapping(target = "idparceiro", source = "entity.parceiro.id")
    @Mapping(target = "nomeparc", source = "entity.parceiro.nome")
    @Mapping(target = "codbco", source = "entity.banco.id")
    @Mapping(target = "indicadorpix", source = "indicadorPix")
    @Mapping(target = "tipobase", source = "registrobase")
    ContaDto toDtoContaNaoCredenciada(ContaNaoCredenciada entity);
}
