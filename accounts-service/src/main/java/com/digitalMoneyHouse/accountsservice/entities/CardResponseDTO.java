package com.digitalMoneyHouse.accountsservice.entities;

public class CardResponseDTO {

    private String id;
    private String number;
    private String name;
    private String type;

    public CardResponseDTO() {
    }

    public CardResponseDTO(String id, String number, String name, String type) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
