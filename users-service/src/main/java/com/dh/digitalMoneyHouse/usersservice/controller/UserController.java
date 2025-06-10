package com.dh.digitalMoneyHouse.usersservice.controller;

import com.dh.digitalMoneyHouse.usersservice.entities.AccessKeycloak;
import com.dh.digitalMoneyHouse.usersservice.entities.Login;
import com.dh.digitalMoneyHouse.usersservice.entities.UserIdRequest;
import com.dh.digitalMoneyHouse.usersservice.entities.dto.*;
import com.dh.digitalMoneyHouse.usersservice.exceptions.BadRequestException;
import com.dh.digitalMoneyHouse.usersservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/hola")
    public String saludar() {
        return "Hola mundo";
    }

    //  este metodo obtiene por el usuario por ID de Keycloak
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {

        try {
            //  Si  es un numero, entonces es el id de usuario de la tabla
            Integer.parseInt(id); // o Long.parseLong / Double.parseDouble según el caso
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByOriginalId(Long.parseLong(id)));
        } catch (NumberFormatException e) {
            //  Si no es un numero, entonces es el id de usuario de Keycloak
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
        }


    }

    //  este metodo obtiene por el id original del usuario
    @GetMapping("/userOriginal/{id}")
    public ResponseEntity<?> getOriginalUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByOriginalId(id));
    }


//    //  este metodo obtiene por el id original del usuario
//    @GetMapping("/user/{id}")
//    public ResponseEntity<?> getOriginalUserById2(@PathVariable Long id) {
//        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByOriginalId(id));
//    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRegistrationDTO userRegistrationDTO) throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userRegistrationDTO)); // ahora devuelve un AuthResponseDTO

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login loginData) throws Exception{
        AccessKeycloak credentials = userService.login(loginData);

        if (credentials != null) {
            return ResponseEntity.ok(credentials);
        } else if (userService.findByEmail(loginData.getEmail()).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userId.isEmpty()) {
            ResponseEntity.notFound().build();
        }

        userService.logout(userId);

        return ResponseEntity.ok("Succesfully logged out");
    }

    @PutMapping("/{username}/forgot-password")
    public void forgotPassword(@PathVariable String username) {
        userService.forgotPassword(username);
    }

    @PatchMapping("/update-alias")
    public ResponseEntity<?> updateAlias(@RequestBody NewAliasRequest newAlias) throws BadRequestException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateAlias(kcId,newAlias);
        return ResponseEntity.ok("Alias updated succesfully");
    }

    @PatchMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest userUpdateRequest) throws Exception {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userUpdated = userService.updateUser(kcId, userUpdateRequest);

        if(userUpdated == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody NewPasswordRequest passwordRequest) throws BadRequestException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updatePassword(kcId, passwordRequest);

        return ResponseEntity.ok("Password updated succesfully");
    }

    @GetMapping("/keycloak-id/{kcId}")
    public ResponseEntity<?> getUserByKeycloakId(@PathVariable String kcId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserIdByKcId(kcId));
    }

    @GetMapping("/alias/{alias}")
    public ResponseEntity<?> getUserIdByAlias(@PathVariable String alias) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserIdByAlias(alias));
    }

    @GetMapping("/cvu/{cvu}")
    public ResponseEntity<?> getUserIdByCvu(@PathVariable String cvu) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserIdByCvu(cvu));
    }
}

