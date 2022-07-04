package com.golikov.bank.domain.currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {

    private String CharCode;

    private String Name;

    private BigDecimal Value;

    private Integer Nominal;
}
