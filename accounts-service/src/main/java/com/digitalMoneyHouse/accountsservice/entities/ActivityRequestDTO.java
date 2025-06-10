package com.digitalMoneyHouse.accountsservice.entities;

import java.util.Date;

public class ActivityRequestDTO {

    private Double amount;
    private String type;
    private String description;
    private Date dated;
    private String destination;

    public ActivityRequestDTO(Double amount, String type, String description, Date dated, String destination) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.dated = dated;
        this.destination = destination;
    }

    // Constructor vacío (requerido por frameworks como Jackson)
    public ActivityRequestDTO() {
    }



    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    // Getters y Setters

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDated() {
        return dated;
    }

    public void setDated(Date dated) {
        this.dated = dated;
    }
}

