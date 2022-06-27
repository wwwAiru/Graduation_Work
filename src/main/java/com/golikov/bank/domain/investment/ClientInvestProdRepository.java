package com.golikov.bank.domain.investment;

import com.golikov.bank.domain.investment.ClientInvestProd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClientInvestProdRepository extends JpaRepository<ClientInvestProd, Long> {
}
