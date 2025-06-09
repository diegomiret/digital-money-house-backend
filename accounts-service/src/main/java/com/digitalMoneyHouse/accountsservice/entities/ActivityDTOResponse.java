package com.digitalMoneyHouse.accountsservice.entities;

public class ActivityDTOResponse {
    private Double amount;
    private String type;

    public ActivityDTOResponse() {
    }

    public ActivityDTOResponse(Double amount, String type) {
        this.amount = amount;
        this.type = type;
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
