package com.golikov.bank.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "")
    private List<UserInvestProd> userInvestProds;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.REFRESH})
    private List<DepositAccount> depositAccounts;

    public User() {
    }

    public User(String firstName, String lastName, String middleName, String email, String password, BigDecimal balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<UserInvestProd> getUserInvestProds() {
        return userInvestProds;
    }

    public List<DepositAccount> getDepositAccounts() {
        return depositAccounts;
    }

    public void AddUserInvestProd(UserInvestProd userInvestProd) {
        if (userInvestProds == null) {
            userInvestProds = new ArrayList<>();
        }
        userInvestProds.add(userInvestProd);
        userInvestProd.setUser(this);
    }
}
