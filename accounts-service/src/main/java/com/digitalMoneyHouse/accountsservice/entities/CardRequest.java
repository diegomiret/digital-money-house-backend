package com.digitalMoneyHouse.accountsservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardRequest {
    private String name;
    private String number;
    private String expiration;
    private String cvc;
}
