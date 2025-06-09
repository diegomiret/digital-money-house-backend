package com.digitalMoneyHouse.accountsservice.entities;

import java.util.Date;

public class ActivityRequestDTO {

    private Double amount;
    private String type;
    private String description;
    private Date dated;

    // Constructor vacío (requerido por frameworks como Jackson)
    public ActivityRequestDTO() {
    }

    // Constructor con todos los campos
    public ActivityRequestDTO(Double amount, String type, String description, Date dated) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.dated = dated;
    }

    // Constructor sin dated (para crear y asignar fecha luego en backend)
    public ActivityRequestDTO(Double amount, String type, String description) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.dated = new Date(); // Se genera la fecha actual automáticamente
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

