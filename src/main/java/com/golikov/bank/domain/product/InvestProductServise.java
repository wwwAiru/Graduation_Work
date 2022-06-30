package com.golikov.bank.domain.product;

import com.golikov.bank.domain.product.dto.InvestProductDto;
import com.golikov.bank.exception.ResourceNotFoundException;
import com.golikov.bank.domain.product.mapper.InvestProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestProductServise {

    @Autowired
    InvestProductRepository investProductRepository;

    private final InvestProductMapper investProductMapper;

    public void save(InvestProduct investProduct){
        investProductRepository.save(investProduct);
    }

    public List<InvestProduct> findAllByActive () {
        return investProductRepository.findAllByIsActiveIsTrueOrderByName();
    }


    public void delete(InvestProduct investProduct){
        investProduct.setActive(false);
        investProductRepository.save(investProduct);
    }


    public InvestProduct findById(InvestProduct investProduct){
        return investProductRepository.findById(investProduct.getId()).get();
    }

    public List<InvestProductDto> findAllRest () {
        List<InvestProduct> investProducts = investProductRepository.findAllByOrderById();
        return investProductMapper.toDtoList(investProducts);
    }

    public InvestProductDto findByIdRest(Long id){
        InvestProduct investProduct = investProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Инвестиционный продукт с id: " + id + " не найден"));
        return investProductMapper.toDto(investProduct);
    }

    public InvestProductDto saveRest(Long id, InvestProductDto investProductDto){
        investProductDto.setId(id);
        InvestProduct investProductEdited = investProductMapper.toEntity(investProductDto);
        investProductRepository.save(investProductEdited);
        return investProductDto;
    }


}
