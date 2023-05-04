package br.com.boleto.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.boleto.persistence.dtos.ContaDto;
import br.com.boleto.persistence.dtos.ContaCredenciamentoDto;
import br.com.boleto.persistence.dtos.ContaPesquisaDto;
import br.com.boleto.persistence.dtos.ContaResponseDto;
import br.com.boleto.persistence.entity.Conta;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContaMapper {
	@Mapping(target = "idparceiro", source = "entity.parceiro.id")
	@Mapping(target = "nomeparc", source = "entity.parceiro.nome")
	@Mapping(target = "codbco", source = "entity.banco.id")
	@Mapping(target = "tipobase", source = "registrobase")
	@Mapping(target = "id", source = "id")
    ContaPesquisaDto toDtoContaPesquisa(Conta entity);

    @Mapping(target = "parceiro.id", source = "idparceiro")
    @Mapping(target = "parceiro.nome", source = "nomeparc")
    @Mapping(target = "registrobase", source = "tipobase")
    @Mapping(target = "banco.id", source = "codbco")
    @Mapping(target = "indicadorPix", source = "indicadorpix")
    Conta toEntityContaDto(ContaDto dto);
    
    @Mapping(target = "idparceiro", source = "entity.parceiro.id")
    @Mapping(target = "nomeparc", source = "entity.parceiro.nome")
    @Mapping(target = "codbco", source = "entity.banco.id")
    @Mapping(target = "indicadorpix", source = "indicadorPix")
    @Mapping(target = "tipobase", source = "registrobase")
    ContaDto toDtoConta(Conta entity);
    
    @Mapping(target = "idparceiro", source = "entity.parceiro.id")
    @Mapping(target = "nomeparc", source = "entity.parceiro.nome")
    @Mapping(target = "codbco", source = "entity.banco.id")
	@Mapping(target = "tipobase", source = "registrobase")
    ContaResponseDto toDtoContaResponse(Conta entity);

    @Mapping(target = "parceiro.id", source = "idparceiro")
    @Mapping(target = "parceiro.nome", source = "nomeparc")
    @Mapping(target = "registrobase", source = "tipobase")
    @Mapping(target = "banco.id", source = "codbco")
    @Mapping(target = "indicadorPix", source = "indicadorpix")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientid", ignore = true)
    @Mapping(target = "clientsecret", ignore = true)
    void mergeDtoToContaSincronizada(ContaDto contaDto, @MappingTarget Conta conta);

    @Mapping(target = "parceiro.id", source = "idparceiro")
    @Mapping(target = "parceiro.nome", source = "nomeparc")
    @Mapping(target = "registrobase", source = "tipobase")
    @Mapping(target = "banco.id", source = "codbco")
    @Mapping(target = "indicadorPix", source = "indicadorpix")
    void mergeContaCredenciamentoDtoToConta(ContaCredenciamentoDto contaCredenciamentoDto, @MappingTarget Conta conta);

    @Mapping(target = "parceiro.id", source = "idparceiro")
    @Mapping(target = "parceiro.nome", source = "nomeparc")
    @Mapping(target = "registrobase", source = "tipobase")
    @Mapping(target = "banco.id", source = "codbco")
    @Mapping(target = "indicadorPix", source = "indicadorpix")
    Conta toEntityContaCredenciamentoDto(ContaCredenciamentoDto contaCredenciamentoDto);

}
