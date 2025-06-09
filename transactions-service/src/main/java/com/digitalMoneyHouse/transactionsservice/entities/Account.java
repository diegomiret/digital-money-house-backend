package com.digitalMoneyHouse.transactionsservice.entities;

import jakarta.persistence.Transient;
import lombok.Data;

import java.util.List;

@Data
public class Account {
    private Long id;
    private Long userId;
    private Double balance;


    public Account(Long userId, Double balance) {
        this.userId = userId;
        this.balance = balance;
    }

}
