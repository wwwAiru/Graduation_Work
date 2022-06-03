package com.golikov.bank;

import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.entity.User;
import com.golikov.bank.entity.UserInvestProd;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class TestEntityMain {
    public static void main(String[] args) {
        LocalDateTime createAt;
        LocalDateTime endDate;

        User user = new User(
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
        UserInvestProd userInvestProd = new UserInvestProd(
                new BigDecimal(10000000),
                user,
                investProduct,
                createAt = LocalDateTime.now(),
                endDate = LocalDateTime.now());
        user.AddUserInvestProd(userInvestProd);
        System.out.println(user.getUserInvestProds());


    }
}
