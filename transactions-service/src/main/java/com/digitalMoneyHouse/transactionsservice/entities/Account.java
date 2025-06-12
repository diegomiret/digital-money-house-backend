package com.digitalMoneyHouse.transactionsservice.entities;

import jakarta.persistence.Transient;
import lombok.Data;

import java.util.List;

@Data
public class Account {
    private Long id;
    private Long userId;
    private Double balance;
    private String cvu;
    private String alias;


    public Account(Long userId, Double balance, String cvu, String alias) {
        this.userId = userId;
        this.balance = balance;
        this.cvu = cvu;
        this.alias = alias;
    }
}
