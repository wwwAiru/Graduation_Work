package com.golikov.bank.domain.product.mapper;

import com.golikov.bank.domain.product.InvestProduct;
import com.golikov.bank.domain.product.dto.InvestProductResp;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvestProductMapper {

    InvestProductResp toResp(InvestProduct investProduct);

    InvestProduct toEntity(InvestProductResp investProductResp);

    List<InvestProductResp> toRespList(List<InvestProduct> investProducts);
}
