package com.golikov.bank.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientTransaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_type")
    private String transactionType;

    @NotBlank(message = "Обязательное поле")
    @NotNull
    @Column(name = "owner_name")
    private String ownerName;

    @NotBlank(message = "Обязательное поле")
    @NotNull
    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "amount")
    @DecimalMin(value = "1", message = "Значение неможет быть меньше 1")
    @DecimalMax(value = "10000000000", message = "Превышено максимальное значение")
    private BigDecimal amount;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "client_id")
    private Long clientId;

}
