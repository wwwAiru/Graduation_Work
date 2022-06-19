package com.golikov.bank.service;

import com.golikov.bank.entity.Currency;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Getter
@Setter
@EnableScheduling
public class CurrencyService {

    @Autowired
    Currency currency;

    private Map<String, Currency> currencies;

    private Currency usd = currencies.get("USD");

    private Currency eur = currencies.get("EUR");

    public CurrencyService() {
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
            this.currencies = currenciesData.keySet()
                    // Преобразовывает Set в Stream.
                    .stream()
                    // При помощи GSON переводит JSON строчку валюты к классу Currency.
                    .map((currency) -> new Gson().fromJson(currenciesData.getJSONObject(currency).toString(), Currency.class))
                    // Преобразовывает Stream в Map с ключом - названием валюты(CharCode), значением - объект валюты (Currency).
                    .collect(Collectors.toMap(c-> c.getCharCode(), c -> c));

            System.out.println("updated");

        } catch (Exception ignored) { }
    }


}
