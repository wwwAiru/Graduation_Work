package com.golikov.bank.domain.currency;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


// этот вспомогательный класс для удобства работы с валютой
@Component
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CurrencyHolder {

    private Currency currency;

    private Map<String, Currency> currencies;

    // Устанавливаются дефолтные значения Евро и Доллара на случай если не получилось получить список валют с сайта ЦБ
    @PostConstruct
    private void defaultCurrencies(){
        if (currencies==null) currencies = new HashMap<>();
        Currency currencyUSD = new Currency("USD", "Доллар США", BigDecimal.valueOf(57.8021), 1);
        this.currencies.put("USD", currencyUSD);
        Currency currencyEUR = new Currency("EUR", "Евро", BigDecimal.valueOf(61.3718), 1);
        this.currencies.put("EUR", currencyEUR);
        System.out.println("Set default values to currencies");
    }
}
