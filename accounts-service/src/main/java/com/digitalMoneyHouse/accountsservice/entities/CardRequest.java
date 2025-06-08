package com.digitalMoneyHouse.accountsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class CardRequest {
    private String holder;
    private String number;
    private LocalDate expirationDate;
    private String cvv;
}
