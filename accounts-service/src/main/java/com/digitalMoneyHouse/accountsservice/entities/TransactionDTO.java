package com.digitalMoneyHouse.accountsservice.entities;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class TransactionDTO {

    private String id;
    private double amount;
    private String name; // Opcional
    private ZonedDateTime dated;
    private TransactionType type;
    private String origin; // Opcional
    private String destination; // Opcional


    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getDated() {
        return dated;
    }

    public void setDated(ZonedDateTime dated) {
        this.dated = dated;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
