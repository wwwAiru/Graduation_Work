package com.golikov.bank.domain.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    private String CharCode;

    private String Name;

    private BigDecimal Value;

    private Integer Nominal;
}
