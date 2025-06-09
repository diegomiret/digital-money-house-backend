package com.digitalMoneyHouse.cardsservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long accountId;
    private String name;
    private String number;
    private String expiration;
    private String cvc;

    public Card(Long accountId, String name, String number, String expiration, String cvc) {
        this.accountId = accountId;
        this.name = name;
        this.number = number;
        this.expiration = expiration;
        this.cvc = cvc;
    }
}
