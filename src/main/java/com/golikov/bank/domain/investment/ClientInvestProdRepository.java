package com.golikov.bank.domain.investment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientInvestProdRepository extends JpaRepository<ClientInvestProd, Long> {
/**
 * JPQL запрос для фильтрации на странице в админке "инвестиции клиентов"
 * @param keyword - строка для фильтрации поиска
*/
    @Query("SELECT cip FROM ClientInvestProd cip WHERE lower(cip.account.client.email) LIKE lower(concat('%', ?1,'%'))" +
            "OR lower(cip.account.client.firstName) LIKE lower(concat('%', ?1,'%'))" +
            "OR lower(cip.account.client.lastName) LIKE lower(concat('%', ?1,'%'))" +
            "OR cip.account.client.middleName LIKE lower(concat('%', ?1,'%'))" +
            "OR lower(cip.investProduct.name) LIKE lower(concat('%', ?1,'%'))")
    List<ClientInvestProd> findAllByKeyword(String keyword);
}
