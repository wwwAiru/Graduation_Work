package com.golikov.bank.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "invest_products")
@Getter
@Setter
public class InvestProduct {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Обязательное поле")
    @Size(min = 10, max = 100, message = "Введите название, минимум 10 символов, максимум 100")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Обязательное поле")
    @Size(min = 20, max = 255, message = "Введите описание, минимум 20 символов, максимум 255")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "Обязательное поле")
    @Size(message = "Выберите валюту(по умолчанию валюта - RUB)")
    @Column(name = "currency")
    private String currency;

    @NotNull(message = "Обязательное поле")
    @DecimalMin(value = "0", message = "Значение не может быть отрицательным")
    @DecimalMax(value = "100000000", message = "Максимальное значение ограничено банком 100 000 000")
    @Column(name = "min_deposit")
    private BigDecimal minDeposit;

    @NotNull(message = "Обязательное поле")
    @DecimalMin(value = "0", message = "Значение не может быть отрицательным")
    @DecimalMax(value = "100000000", message = "Максимальное значение ограничено банком 100 000 000")
    @Column(name = "max_deposit")
    private BigDecimal maxDeposit;

    @NotNull(message = "Обязательное поле")
    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @OneToMany(mappedBy = "investProduct")
    private List<ClientInvestProd> clientInvestProd;

    @NotNull(message = "Обязательное поле")
    @Column(name = "min_deposit_term")
    private Long minDepositTerm;

    @NotNull(message = "Обязательное поле")
    @Column(name = "max_deposit_term")
    private Long maxDepositTerm;

    @NotNull(message = "выберите актуальность продукта(по умолчанию 'Активный')")
    @Column(name = "is_active")
    private boolean isActive;


    public InvestProduct() {
    }

    public InvestProduct(String name, String description, String currency, BigDecimal minDeposit, BigDecimal maxDeposit, BigDecimal interestRate, boolean isActive, List<ClientInvestProd> clientInvestProd, Long minDepositTerm, Long maxDepositTerm) {
        this.name = name;
        this.description = description;
        this.currency = currency;
        this.minDeposit = minDeposit;
        this.maxDeposit = maxDeposit;
        this.interestRate = interestRate;
        this.isActive = isActive;
        this.clientInvestProd = clientInvestProd;
        this.minDepositTerm = minDepositTerm;
        this.maxDepositTerm = maxDepositTerm;
    }
}
