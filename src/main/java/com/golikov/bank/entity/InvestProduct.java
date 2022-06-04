package com.golikov.bank.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "invest_products")
public class InvestProduct {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "min_deposit")
    private BigDecimal minDeposit;

    @Column(name = "max_deposit")
    private BigDecimal maxDeposit;

    @Column(name = "iterest_rate")
    private BigDecimal interestRate;

    @OneToMany(mappedBy = "investProduct", cascade = {CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH})
    private List<ClientInvestProd> clientInvestProd;

    @Column(name = "deposit_term")
    private Long depositTerm;

    public InvestProduct() {
    }

    public InvestProduct(String name, String description, BigDecimal minDeposit, BigDecimal maxDeposit, BigDecimal interestRate, Long depositTerm) {
        this.name = name;
        this.description = description;
        this.minDeposit = minDeposit;
        this.maxDeposit = maxDeposit;
        this.interestRate = interestRate;
        this.depositTerm = depositTerm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "InvestProduct{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", minDeposit=" + minDeposit +
                ", maxDeposit=" + maxDeposit +
                ", interestRate=" + interestRate +
                ", depositTerm=" + depositTerm +
                '}';
    }
}
