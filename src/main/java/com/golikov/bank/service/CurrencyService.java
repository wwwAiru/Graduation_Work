package com.golikov.bank.service;

import com.golikov.bank.utils.Currency;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@EnableScheduling
@Service
@Getter
@Setter
public class CurrencyService {

    private Map<String, Currency> currencies;

    public CurrencyService() {
    }

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

    //установил выполнение этого метода 1 раз в 4 часа
    @Scheduled(fixedDelay = 1000*60*60*4)
    public void update() {
        try {
            // Преобразует массив байт в строку.
            String json = new String(
                    // Создаёт URL со ссылкой на API ЦБ.
                    new URL("https://www.cbr-xml-daily.ru/daily_json.js")
                            // Открывает поток получения данных.
                            .openStream()
                            // Считывает все данные в виде массива байт.
                            .readAllBytes()
            );

            // Парсит JSON и получает данные валют.
            JSONObject currenciesData = new JSONObject(json)
                    .getJSONObject("Valute");

            // Получает коды валют из полученных данных.
            currencies = currenciesData.keySet()
                    // Преобразовывает Set в Stream.
                    .stream()
                    // При помощи GSON переводит JSON строчку валюты к классу Currency.
                    .map((currency) -> new Gson().fromJson(currenciesData.getJSONObject(currency).toString(), Currency.class))
                    // Преобразовывает Stream в Map с ключом - названием валюты(CharCode), значением - объект валюты (Currency).
                    .collect(Collectors.toMap(c-> c.getCharCode(), c -> c));

            System.out.println("Currencies updated");

        } catch (Exception ignored) { }
    }


}
