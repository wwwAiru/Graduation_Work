package com.golikov.bank.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @Column(name = "balance")
    @NotNull(message = "Поле не может быть пустым")
    @DecimalMin(value = "1", message = "Значение неможет быть меньше 1")
    @DecimalMax(value = "1000000000000", message = "Превышено максимальное значение")
    private BigDecimal balance;


    @Column(name = "profit")
    private BigDecimal profit;


    @Min(value = 0, message = "Поле не может содержать отрицательное число")
    @Max(value = 3640, message = "Вы превысили допустимое значение")
    @NotNull(message = "Поле не может быть пустым")
    private Integer days;

    @ManyToOne()
    @JoinColumn(name = "deposit_acc_id")
    private DepositAccount depositAccount;

    @ManyToOne()
    @JoinColumn(name = "invest_prod_id")
    private InvestProduct investProduct;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    public ClientInvestProd() {
    }

    public ClientInvestProd(BigDecimal balance, DepositAccount depositAccount, InvestProduct investProduct, LocalDateTime beginDate, LocalDateTime expireDate) {
        this.balance = balance;
        this.depositAccount = depositAccount;
        this.investProduct = investProduct;
        this.beginDate = beginDate;
        this.expireDate = expireDate;
    }
}
