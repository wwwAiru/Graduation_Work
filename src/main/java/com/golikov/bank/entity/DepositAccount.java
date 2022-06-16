package com.golikov.bank.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "deposit_accounts")
@Getter
@Setter
public class DepositAccount {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "client_description")
    private String description;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "currency")
    private String currency;

    @Column(name = "deposit_balance")
    private BigDecimal depositBalance = new BigDecimal(0);

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "depositAccount", fetch = FetchType.EAGER)
    private Set<ClientInvestProd> clientInvestProds;

    public DepositAccount() {
    }

    public DepositAccount(String description, String currency, Client client) {
        this.description = description;
        this.currency = currency;
        this.client = client;
    }

    public void AddClientInvestProd(ClientInvestProd clientInvestProd) {
        if (clientInvestProds == null) {
            clientInvestProds = new HashSet<>();
        }
        clientInvestProd.setDepositAccount(this);
        clientInvestProds.add(clientInvestProd);
    }
}
