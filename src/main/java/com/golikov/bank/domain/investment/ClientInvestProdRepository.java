package com.golikov.bank.domain.investment;

import com.golikov.bank.domain.investment.ClientInvestProd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientInvestProdRepository extends JpaRepository<ClientInvestProd, Long> {

    @Query("SELECT cip FROM ClientInvestProd cip WHERE cip.account.client.email LIKE %?1% " +
            "OR cip.account.client.firstName LIKE %?1% " +
            "OR cip.account.client.lastName LIKE %?1% OR cip.account.client.middleName LIKE %?1% " +
            "OR cip.investProduct.name LIKE %?1%")
    public List<ClientInvestProd> findAllKeyword(String keyword);
}
