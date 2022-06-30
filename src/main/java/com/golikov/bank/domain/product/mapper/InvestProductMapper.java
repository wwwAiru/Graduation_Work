package com.golikov.bank.domain.product.mapper;

import com.golikov.bank.domain.product.InvestProduct;
import com.golikov.bank.domain.product.dto.InvestProductDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvestProductMapper {

    InvestProductDto toDto(InvestProduct investProduct);

    InvestProduct toEntity(InvestProductDto investProductDto);

    List<InvestProductDto> toDtoList(List<InvestProduct> investProducts);
}
