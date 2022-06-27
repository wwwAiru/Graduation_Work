package com.golikov.bank.domain.account.transaction;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_transaction")
@Getter
@Setter
public class ClientTransaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_type")
    private String transactionType;

    @NotBlank(message = "Обязательное поле")
    @Column(name = "owner_name")
    private String ownerName;

    @NotBlank(message = "Обязательное поле")
    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "amount")
    @NotNull(message = "Поле не может быть пустым")
    @DecimalMin(value = "1", message = "Значение неможет быть меньше 1")
    @DecimalMax(value = "1000000000000", message = "Превышено максимальное значение")
    private BigDecimal amount;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "client_id")
    private Long clientId;

    public ClientTransaction() {
    }

    public ClientTransaction(String ownerName, String cardNumber, BigDecimal amount, LocalDateTime date, Long clientId) {
        this.ownerName = ownerName;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.date = date;
        this.clientId = clientId;
    }

}
