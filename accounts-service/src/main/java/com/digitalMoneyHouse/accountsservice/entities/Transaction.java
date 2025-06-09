package com.digitalMoneyHouse.accountsservice.entities;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long id;
    private int senderId;
    private int receiverId;
    private Double amountOfMoney;
    private LocalDateTime date;

    private String type;
    private String description;


    public Transaction(int senderId, int receiverId, Double amountOfMoney, LocalDateTime date, String type, String description) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amountOfMoney = amountOfMoney;
        this.date = date;
        this.type = type;
        this.description = description;
    }

    public Transaction(int senderId, int receiverId, Double amountOfMoney, LocalDateTime date) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amountOfMoney = amountOfMoney;
        this.date = date;
    }
}
