package com.golikov.bank.repository;

import com.golikov.bank.entity.InvestProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvestProdRepository extends JpaRepository<InvestProduct, Long> {
    //сортировка результатов инвест продуктов по уменьшению доходности
    List<InvestProduct> findAllByOrderByInterestRateDesc();
}
