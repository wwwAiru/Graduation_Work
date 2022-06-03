package com.golikov.bank.repository;

import com.golikov.bank.entity.InvestProduct;
import org.springframework.data.repository.CrudRepository;

public interface InvestProdRepository extends CrudRepository<InvestProduct, Long> {
}
