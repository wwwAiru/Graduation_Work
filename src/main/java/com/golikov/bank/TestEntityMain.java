package com.golikov.bank;

import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientInvestProd;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class TestEntityMain {
    public static void main(String[] args) {
        LocalDateTime createAt;
        LocalDateTime endDate;

        Client client = new Client(
                "Первый",
                "Второвов",
                "Третьевич",
                "zato@yandex.ru",
                "pass",
                new BigDecimal(100000000));
        InvestProduct investProduct = new InvestProduct(
                "Простой вклад",
                "Это простой вклад под ставку 11% годовых",
                new BigDecimal(1000),
                new BigDecimal(1000),
                new BigDecimal(1.11),
                10000000L);
        ClientInvestProd clientInvestProd = new ClientInvestProd(
                new BigDecimal(10000000),
                client,
                investProduct,
                createAt = LocalDateTime.now(),
                endDate = LocalDateTime.now());
        client.AddClientInvestProd(clientInvestProd);
        System.out.println(client.getClientInvestProds());


    }
}
