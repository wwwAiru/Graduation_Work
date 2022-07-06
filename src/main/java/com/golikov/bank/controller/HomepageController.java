package com.golikov.bank.controller;

import com.golikov.bank.domain.currency.CurrencyHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Controller
@AllArgsConstructor
public class HomepageController {

    private CurrencyHolder currencyHolder;

    @GetMapping("/")
    public String homepage(Model model) {
        // список валют на главную страницу
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime date = LocalDateTime.now();
        String formattedDate = date.format(formatter);
        model.addAttribute("currencies", new ArrayList<>(currencyHolder.getCurrencies().values()));
        model.addAttribute("date", formattedDate);
        return "homepage";
    }
}
