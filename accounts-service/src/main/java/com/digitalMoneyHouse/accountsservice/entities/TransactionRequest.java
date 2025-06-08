package com.digitalMoneyHouse.accountsservice.entities;

import lombok.Data;

@Data
public class TransactionRequest {
    private String destinyAccount;
    private Double amount;
}
