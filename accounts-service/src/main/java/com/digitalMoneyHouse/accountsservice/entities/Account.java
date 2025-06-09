package com.digitalMoneyHouse.accountsservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Accounts")
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "UserID", nullable = false)
    private Long userId;

    @Column(name = "Balance",nullable = false)
    private Double balance;

    @Column(name = "alias", nullable = false)
    private String alias;

    @Column(name = "cvu", nullable = false)
    private String cvu;


    @Transient
    private List<Transaction> transactions;

    public Account(Long userId) {
        this.userId = userId;
        this.balance = 0.0;
    }

    public Account(Long userId, String alias, String cvu) {
        this.userId = userId;
        this.alias = alias;
        this.cvu = cvu;
        this.balance = 0.0;
    }


    public Account(Long userId, Double balance) {
        this.userId = userId;
        this.balance = balance;
    }

}
