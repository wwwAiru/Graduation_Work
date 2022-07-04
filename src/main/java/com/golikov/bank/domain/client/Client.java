package com.golikov.bank.domain.client;

import com.golikov.bank.domain.account.Account;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clients")
@Data
@RequiredArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Обязательное поле")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "balance")
    private BigDecimal balance = new BigDecimal(0);

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<Account> accounts;

    @Column(name = "active")
    private boolean isActive;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "clients_roles", joinColumns = @JoinColumn(name = "client_id"))
    @Enumerated(EnumType.STRING) //тип энам, строковый
    private Set<Role> roles;


    //для html шаблона
    public String getFullName(){
        return lastName + " " + firstName + " " + middleName ;
    }

}
