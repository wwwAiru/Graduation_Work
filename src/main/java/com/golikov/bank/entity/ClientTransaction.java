package com.golikov.bank.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "amount")
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
