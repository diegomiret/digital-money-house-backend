package com.digitalMoneyHouse.accountsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Transaction {

    private Long id;
    private int senderId;
    private int receiverId;
    private Double amountOfMoney;
    private LocalDateTime date;

    public Transaction(int senderId, int receiverId, Double amountOfMoney, LocalDateTime date) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amountOfMoney = amountOfMoney;
        this.date = date;
    }
}
