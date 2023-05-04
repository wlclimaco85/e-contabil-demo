package br.com.boleto.persistence.mapper;

import br.com.boleto.persistence.dtos.AcoesDto;
import br.com.boleto.persistence.entity.Banco;
import org.mapstruct.Mapper;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface BancoMapper {
    AcoesDto toDtoBanco(Banco entity);

    ArrayList<AcoesDto> toDtoListBanco(ArrayList<Banco> entity);
}
