package com.golikov.bank.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "name")
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "currency")
    private String currency;

    @Column(name = "min_deposit")
    private BigDecimal minDeposit;

    @Column(name = "max_deposit")
    private BigDecimal maxDeposit;

    @Column(name = "iterest_rate")
    private BigDecimal interestRate;

    @OneToMany(mappedBy = "investProduct")
    private List<ClientInvestProd> clientInvestProd;

    @Column(name = "min_deposit_term")
    private Long minDepositTerm;

    @Column(name = "max_deposit_term")
    private Long maxDepositTerm;

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
