package com.golikov.bank.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


//этот вспомогательный класс для CurrencyService и удобства работы в валютой
@Data
@AllArgsConstructor
public class Currency {

    private String CharCode;

    private String Name;

    private BigDecimal Value;

    private Integer Nominal;

}
