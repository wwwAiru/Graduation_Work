package com.golikov.bank.controller;

import com.golikov.bank.domain.currency.Currency;
import com.golikov.bank.domain.currency.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    CurrencyService currencyService;


    @GetMapping("/")
    public String homepage(Model model) {
        // список валют на главную страницу
        Map <String, Currency> currencyMap = currencyService.getCurrencies();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime date = LocalDateTime.now();
        String formattedDate = date.format(formatter);
        model.addAttribute("currencies", currencyMap.values().stream().toList());
        model.addAttribute("date", formattedDate);
        return "homepage";
    }
}
