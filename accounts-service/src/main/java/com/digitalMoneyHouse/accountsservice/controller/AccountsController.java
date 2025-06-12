package com.digitalMoneyHouse.accountsservice.controller;

import com.digitalMoneyHouse.accountsservice.entities.*;
import com.digitalMoneyHouse.accountsservice.exceptions.BadRequestException;
import com.digitalMoneyHouse.accountsservice.exceptions.ResourceNotFoundException;
import com.digitalMoneyHouse.accountsservice.exceptions.TransactionGoneException;
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

    //  obtiene todas las cuentas
    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccounts() throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAllAccounts());
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

    //  Obiene las transacciones todas o la que indique la variable _limit
    @GetMapping("/users/{userId}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String userId, @RequestParam(name = "_limit", required = false) Integer limit) throws ResourceNotFoundException {
        //String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userOriginal=  accountsService.getUserIdByKcId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getLastFiveTransactionsFull(userOriginal, limit));
    }

    @PostMapping("/register-card")
    public ResponseEntity<?> registerNewCard(@RequestBody CardRequest card) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountsService.registerCard(card, userId));
    }


    @PostMapping("/users/{idUser}/cards")
    public ResponseEntity<?> registerCard(@RequestBody CardRequest card) throws ResourceNotFoundException {
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

    @PatchMapping("/users/{userId}/accounts/1")
    public ResponseEntity<Account> patchAccount(
            @PathVariable String userId,
            @RequestBody PatchAccountRequest request) throws ResourceNotFoundException {

        Long userIdX =  accountsService.getUserIdByKcId(userId);
        Account updatedAccount = accountsService.patchAccount(userIdX, request);
        return ResponseEntity.ok(updatedAccount);
    }


    @PostMapping("/send-money")
    public ResponseEntity<?> sendMoney(@RequestBody TransactionRequest transactionRequest) throws BadRequestException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.sendMoney(transactionRequest, userId));
    }

    @GetMapping("/users/{idUser}/cards")
    public ResponseEntity<?> getAllCardsById(@PathVariable String idUser) throws ResourceNotFoundException {
        //String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userIdX =  accountsService.getUserIdByKcId(idUser);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getAllCards(userIdX));
    }


    @DeleteMapping("/users/{idUser}/cards/{idCard}")
    public ResponseEntity<?> deleteIdCard(@PathVariable String idUser, @PathVariable Long idCard) throws ResourceNotFoundException {
        //String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userIdX=  accountsService.getUserIdByKcId(idUser);

        return ResponseEntity.status(HttpStatus.OK).body(accountsService.deleteCardById(idCard, userIdX));
        //return ResponseEntity.ok().build();
    }


    @PostMapping("/users/{idUser}/activities")
    public ResponseEntity<?> AddMoney(@RequestBody ActivityRequestDTO request, @PathVariable String idUser) throws ResourceNotFoundException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId=  accountsService.getUserIdByKcId(kcId);
        //accountsService.addActivity(request, userId);
        //return ResponseEntity.status(HttpStatus.OK).body(accountsService.addActivity(request, userId));


        try {
            Transaction created = accountsService.addActivity(request, userId);
            return ResponseEntity.ok(created);
        } catch (TransactionGoneException ex) {
            return ResponseEntity.status(HttpStatus.GONE).body("Saldo insuficiente");
        }

        //return ResponseEntity.status(HttpStatus.OK).body(accountsService.addActivity(request, userId));
    }


    //  Obtiene una actividad
    @GetMapping("/users/{userId}/activities/{idTransaction}")
    public ResponseEntity<?> getActivity(@PathVariable String userId, @PathVariable long idTransaction) throws ResourceNotFoundException {
        //String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userOriginal=  accountsService.getUserIdByKcId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(accountsService.getTransaction(userOriginal, idTransaction));
    }
}
