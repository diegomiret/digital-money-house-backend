package com.digitalMoneyHouse.accountsservice.entities;

public class PatchAccountRequest {
    private String alias;

    public PatchAccountRequest() {}

    public PatchAccountRequest(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
