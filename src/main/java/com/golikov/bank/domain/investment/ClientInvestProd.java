package com.golikov.bank.domain.investment;

import com.golikov.bank.domain.account.Account;
import com.golikov.bank.domain.product.InvestProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "client_invest_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "days")
    @Min(value = 0, message = "Поле не может содержать отрицательное число")
    @Max(value = 3640, message = "Вы превысили допустимое значение")
    @NotNull(message = "Поле не может быть пустым")
    private Integer days;

    @ManyToOne()
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne()
    @JoinColumn(name = "invest_prod_id")
    private InvestProduct investProduct;

    @Column(name = "begin_date")
    private LocalDateTime beginDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

}
