package com.golikov.bank.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity
@Table(name = "client_invest_products")
@Getter
@Setter
public class ClientInvestProd {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "product_balance")
    private BigDecimal productBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_acc_id")
    private DepositAccount depositAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invest_prod_id")
    private InvestProduct investProduct;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    public ClientInvestProd() {
    }

    public ClientInvestProd(BigDecimal productBalance, DepositAccount depositAccount, InvestProduct investProduct, LocalDateTime beginDate, LocalDateTime expireDate) {
        this.productBalance = productBalance;
        this.depositAccount = depositAccount;
        this.investProduct = investProduct;
        this.beginDate = beginDate;
        this.expireDate = expireDate;
    }
}
