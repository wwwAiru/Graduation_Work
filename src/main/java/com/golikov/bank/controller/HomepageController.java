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
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private CurrencyHolder currencyHolder;

    @GetMapping("/")
    public String homepage(Model model) {
        // список валют на главную страницу
        String formattedDate = FORMATTER.format(LocalDateTime.now());
        model.addAttribute("currencies", new ArrayList<>(currencyHolder.getCurrencies().values()));
        model.addAttribute("date", formattedDate);
        return "homepage";
    }
}
