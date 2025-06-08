package com.digitalMoneyHouse.accountsservice.controller;

import com.digitalMoneyHouse.accountsservice.entities.*;
import com.digitalMoneyHouse.accountsservice.exceptions.BadRequestException;
import com.digitalMoneyHouse.accountsservice.exceptions.ResourceNotFoundException;
import com.digitalMoneyHouse.accountsservice.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountsController {
    @Autowired
    private AccountsService accountsService;

    @GetMapping("/hola")
    public String saludar() {
        return "Hola mundo";
    }
    @PostMapping("/create")
    public void createAccount(@RequestBody AccountRequest accountRequest) {
        accountsService.createAccount(accountRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAccountInformation(id));
    }

    @GetMapping("/user-information")
    public ResponseEntity<?> getAccount() throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAccountInformation(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable Long id) {
        return new ResponseEntity<>(accountsService.updateAccount(account), HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getLastFiveTransactions () throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getLastFiveTransactions(userId));
    }

    @GetMapping("/activity")
    public ResponseEntity<?> getAllTransactions() throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAllTransactions(userId));
    }

    @GetMapping("/activity/{transactionId}")
    public ResponseEntity<?> getTransaction(@PathVariable Long transactionId) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getTransaction(userId, transactionId));
    }

    //  Obtiene las ultimas 5 transacciones
    @GetMapping("/users/{userId}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String userId) throws ResourceNotFoundException {
        //String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userOriginal=  accountsService.getUserIdByKcId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getLastFiveTransactionsFull(userOriginal));
    }

    @PostMapping("/register-card")
    public ResponseEntity<?> registerNewCard(@RequestBody CardRequest card) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountsService.registerCard(card, userId));
    }

    @GetMapping("/cards")
    public ResponseEntity<?> getAllCards() throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAllCards(userId));
    }

    @GetMapping("/card/{id}")
    public ResponseEntity<?> getCardById(@PathVariable Long id) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getCardById(id, userId));
    }

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<?> getAcountsByIdClient(@PathVariable String userId) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userIdX =  accountsService.getUserIdByKcId(kcId);
        //return ResponseEntity.status(HttpStatus.OK).body(accountsService.getCardById(id, userId));
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAccountsByIdUser(userIdX));
    }


    @DeleteMapping("/delete-card/{cardNumber}")
    public ResponseEntity<?> deleteCardById(@PathVariable String cardNumber) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        accountsService.deleteCardByNumber(cardNumber, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deposit")
    public ResponseEntity<?> depositMoney(@RequestBody DepositMoneyRequest request) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        accountsService.addMoney(request, userId);
        return ResponseEntity.ok("Money was added successfully to your account");
    }

    @PostMapping("/send-money")
    public ResponseEntity<?> sendMoney(@RequestBody TransactionRequest transactionRequest) throws BadRequestException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.sendMoney(transactionRequest, userId));
    }

}
