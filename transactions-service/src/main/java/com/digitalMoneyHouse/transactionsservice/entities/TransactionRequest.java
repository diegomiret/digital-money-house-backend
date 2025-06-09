package com.digitalMoneyHouse.transactionsservice.entities;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionRequest {
    private int senderId;
    private int receiverId;
    private Double amountOfMoney;
    private LocalDateTime date;
    private String type;
    private String description;

}
