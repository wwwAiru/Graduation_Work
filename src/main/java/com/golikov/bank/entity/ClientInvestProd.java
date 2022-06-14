package com.golikov.bank.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity
@Table(name = "client_invest_products")
public class ClientInvestProd {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_deposit")
    private BigDecimal productDeposit;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "invest_prod_id")
    private InvestProduct investProduct;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    public ClientInvestProd() {
    }

    public ClientInvestProd(BigDecimal productDeposit, Client client, InvestProduct investProduct, LocalDateTime beginDate, LocalDateTime expireDate) {
        this.productDeposit = productDeposit;
        this.client = client;
        this.investProduct = investProduct;
        this.beginDate = beginDate;
        this.expireDate = expireDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getProductDeposit() {
        return productDeposit;
    }

    public void setProductDeposit(BigDecimal productDeposit) {
        this.productDeposit = productDeposit;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public InvestProduct getInvestProduct() {
        return investProduct;
    }

    public void setInvestProduct(InvestProduct investProduct) {
        this.investProduct = investProduct;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "ClientInvestProd{" +
                ", productDeposit=" + productDeposit +
                ", client=" + client +
                ", investProduct=" + investProduct +
                ", beginDate=" + beginDate +
                ", expireDate=" + expireDate +
                '}';
    }
}
