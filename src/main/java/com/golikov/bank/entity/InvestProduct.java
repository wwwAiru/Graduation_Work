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

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "investProduct")
    private List<ClientInvestProd> clientInvestProd;

    @Column(name = "deposit_term")
    private Long depositTerm;

    public InvestProduct() {
    }

    public InvestProduct(String name, String description, String currency, BigDecimal minDeposit, BigDecimal maxDeposit, BigDecimal interestRate, Long depositTerm, boolean isActive ) {
        this.name = name;
        this.currency = currency;
        this.description = description;
        this.minDeposit = minDeposit;
        this.maxDeposit = maxDeposit;
        this.interestRate = interestRate;
        this.isActive = isActive;
        this.depositTerm = depositTerm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMinDeposit() {
        return minDeposit;
    }

    public void setMinDeposit(BigDecimal minDeposit) {
        this.minDeposit = minDeposit;
    }

    public BigDecimal getMaxDeposit() {
        return maxDeposit;
    }

    public void setMaxDeposit(BigDecimal maxDeposit) {
        this.maxDeposit = maxDeposit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public List<ClientInvestProd> getClientInvestProd() {
        return clientInvestProd;
    }

    public void setClientInvestProd(List<ClientInvestProd> clientInvestProd) {
        this.clientInvestProd = clientInvestProd;
    }
    public Long getDepositTerm() {
        return depositTerm;
    }

    public void setDepositTerm(Long depositTerm) {
        this.depositTerm = depositTerm;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
