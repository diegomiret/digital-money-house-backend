package com.dh.digitalMoneyHouse.usersservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name",nullable = false)
    //@JsonProperty("firstName")
    private String name;

    @Column(name = "Last Name",nullable = false)
    private String lastName;

    @Column(name= "Username", nullable = false, unique = true)
    private String username;

    @Column(name = "Email",nullable = false, unique = true)
    private String email;

    @Column(name = "dni",nullable = false, unique = true)
    private String dni;

    @Column(name = "Phone Number",nullable = false)
    private String phoneNumber;

    @Column(name = "CVU",nullable = false, unique = true)
    private String cvu;

    @Column(name = "Alias",nullable = false, unique = true)
    private String alias;

    @Column(name = "Password",nullable = false)
    private String password;

    @JsonIgnore
    private String keycloakId;

    public User(String name, String lastName, String username, String email, String phoneNumber, String cvu, String alias, String password, String dni) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cvu = cvu;
        this.alias = alias;
        this.password = password;
        this.dni = dni;
    }

    public static User toUser(UserRepresentation userRepresentation) {
        User user = new User();
        user.setKeycloakId(userRepresentation.getId());
        user.setName(userRepresentation.getUsername());
        user.setName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setEmail(userRepresentation.getEmail());
        return user;
    }
}
