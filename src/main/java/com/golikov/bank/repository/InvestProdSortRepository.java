package com.golikov.bank.repository;

import com.golikov.bank.entity.InvestProduct;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestProdSortRepository extends PagingAndSortingRepository<InvestProduct, Long> {
    List<InvestProduct> findAllByInterestRate(Sort sort);
}
