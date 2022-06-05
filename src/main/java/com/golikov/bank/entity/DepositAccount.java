package com.golikov.bank.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "deposit_accounts")
public class DepositAccount {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "account_number")
    private Long accountNumber;

    @Column(name = "currency")
    private String currency;

    @Column(name = "deposit_balance")
    private BigDecimal depositBalance;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "client_id")
    private Client client;

    public DepositAccount() {
    }

    public DepositAccount(String name, Long accountNumber, String currency, BigDecimal depositBalance, Client client) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.depositBalance = depositBalance;
        this.client = client;
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

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getDepositBalance() {
        return depositBalance;
    }

    public void setDepositBalance(BigDecimal depositBalance) {
        this.depositBalance = depositBalance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
