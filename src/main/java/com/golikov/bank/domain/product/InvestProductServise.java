package com.golikov.bank.domain.product;

import com.golikov.bank.domain.product.dto.InvestProductDto;
import com.golikov.bank.domain.product.mapper.InvestProductMapper;
import com.golikov.bank.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestProductServise {

    @Autowired
    private InvestProductRepository investProductRepository;

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

    public InvestProductDto saveEditedRest(Long id, InvestProductDto investProductDto){
        investProductDto.setId(id);
        InvestProduct investProductEdited = investProductMapper.toEntity(investProductDto);
        investProductRepository.save(investProductEdited);
        return investProductDto;
    }

    public InvestProductDto addNewRest(InvestProductDto investProductDto){
        InvestProduct investProductEdited = investProductMapper.toEntity(investProductDto);
        InvestProduct investProduct = investProductRepository.save(investProductEdited);
        InvestProductDto newInvestProd = investProductMapper.toDto(investProduct);
        return newInvestProd;
    }

    public void deleteRest(Long id) {
        InvestProductDto investProductDto = findByIdRest(id);
        InvestProduct investProduct = investProductMapper.toEntity(investProductDto);
        investProductRepository.delete(investProduct);
    }


}
