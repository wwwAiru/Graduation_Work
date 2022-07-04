package com.golikov.bank.domain.currency;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.stream.Collectors;

// сервис для получения курсов валют с сайта ЦБ
@Service
@EnableScheduling
@NoArgsConstructor
public class CurrencyService {

    @Autowired(required=false)
    private CurrencyHolder currencyHolder;


    //установил выполнение этого метода 1 раз в 4 часа
    @Scheduled(fixedDelay = 1000*60*60*4)
    private void updateCurrencies() {
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
            currencyHolder.setCurrencies(currenciesData.keySet()
                    // Преобразовывает Set в Stream.
                    .stream()
                    // При помощи GSON переводит JSON строчку валюты к классу Currency.
                    .map((currency) -> new Gson().fromJson(currenciesData.getJSONObject(currency).toString(), Currency.class))
                    // Преобразовывает Stream в Map с ключом - названием валюты(CharCode), значением - объект валюты (Currency).
                    .collect(Collectors.toMap(Currency::getCharCode, c -> c)));

            System.out.println("Currencies updated");

        } catch (Exception ignored) { ignored.getStackTrace();}
    }


}
