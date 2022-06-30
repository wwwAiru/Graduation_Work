package com.golikov.bank.domain.product.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Component
public class InvestProductDto {

    private Long id;

    @NotBlank(message = "Обязательное поле")
    @Size(min = 10, max = 100, message = "Введите название, минимум 10 символов, максимум 100")
    private String name;

    @NotBlank(message = "Обязательное поле")
    @Size(min = 20, max = 500, message = "Введите описание, минимум 20 символов, максимум 500")
    private String description;

    @NotBlank(message = "Обязательное поле")
    @Size(message = "Выберите валюту(по умолчанию валюта - RUB)")
    private String currency;

    @NotNull(message = "Обязательное поле")
    @DecimalMin(value = "0", message = "Значение не может быть отрицательным")
    @DecimalMax(value = "100000000", message = "Максимальное значение ограничено банком 100 000 000")
    private BigDecimal minDeposit;

    @NotNull(message = "Обязательное поле")
    @DecimalMin(value = "0", message = "Значение не может быть отрицательным")
    @DecimalMax(value = "100000000", message = "Максимальное значение ограничено банком 100 000 000")
    private BigDecimal maxDeposit;

    @NotNull(message = "Обязательное поле")
    private BigDecimal interestRate;


    @NotNull(message = "Обязательное поле")
    private Long minDepositTerm;

    @NotNull(message = "Обязательное поле")
    private Long maxDepositTerm;

    @NotNull(message = "выберите актуальность продукта")
    private boolean isActive;


    public InvestProductDto() {
    }

    public InvestProductDto(String name, String description, String currency, BigDecimal minDeposit, BigDecimal maxDeposit, BigDecimal interestRate, boolean isActive, Long minDepositTerm, Long maxDepositTerm) {
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.minDeposit = minDeposit;
        this.maxDeposit = maxDeposit;
        this.interestRate = interestRate;
        this.isActive = isActive;
        this.minDepositTerm = minDepositTerm;
        this.maxDepositTerm = maxDepositTerm;
    }
}
