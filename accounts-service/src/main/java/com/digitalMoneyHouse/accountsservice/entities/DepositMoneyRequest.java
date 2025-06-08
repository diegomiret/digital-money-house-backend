package com.digitalMoneyHouse.accountsservice.entities;

import lombok.Data;

@Data
public class DepositMoneyRequest {
    private String cardNumber;
    private Double amount;
}
