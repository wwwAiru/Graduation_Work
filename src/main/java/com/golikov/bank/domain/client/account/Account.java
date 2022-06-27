package com.golikov.bank.domain.client.account;

import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.investment.ClientInvestProd;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Account {

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

    @Column(name = "balance")
    private BigDecimal balance = new BigDecimal(0);

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<ClientInvestProd> clientInvestProds;

    public Account() {
    }

    public Account(String description, String currency, Client client) {
        this.description = description;
        this.currency = currency;
        this.client = client;
    }

    public void addClientInvestProd(ClientInvestProd clientInvestProd) {
        if (clientInvestProds == null) {
            clientInvestProds = new HashSet<>();
        }
        clientInvestProd.setAccount(this);
        clientInvestProds.add(clientInvestProd);
    }
}
