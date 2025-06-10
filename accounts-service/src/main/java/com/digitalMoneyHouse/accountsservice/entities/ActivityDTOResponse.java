package com.digitalMoneyHouse.accountsservice.entities;

import java.time.LocalDateTime;

public class ActivityDTOResponse {
    private Double amount;
    private String type;
    private LocalDateTime dated;
    private String name;

    public ActivityDTOResponse() {
    }

    public ActivityDTOResponse(Double amount, String type) {
        this.amount = amount;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDated() {
        return dated;
    }

    public void setDated(LocalDateTime dated) {
        this.dated = dated;
    }

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
}
