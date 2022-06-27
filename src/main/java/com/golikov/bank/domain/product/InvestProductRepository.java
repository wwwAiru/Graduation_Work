package com.golikov.bank.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestProductRepository extends JpaRepository<InvestProduct, Long> {
    //сортировка результатов инвест продуктов по уменьшению доходности
    List<InvestProduct> findAllByIsActiveIsTrueOrderByName();

    List<InvestProduct> findAllByIsActiveIsFalseOrderById();
}
