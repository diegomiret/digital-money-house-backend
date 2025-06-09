package com.digitalMoneyHouse.transactionsservice.controller;

import com.digitalMoneyHouse.transactionsservice.entities.Account;
import com.digitalMoneyHouse.transactionsservice.entities.Transaction;
import com.digitalMoneyHouse.transactionsservice.entities.TransactionRequest;
import com.digitalMoneyHouse.transactionsservice.exceptions.ResourceNotFoundException;
import com.digitalMoneyHouse.transactionsservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transaction) {

        try{
//            Optional<Account> fromAccountOptional = transactionService.getAccount((long) transaction.getSenderId());
//            Optional<Account> toAccountOptional = transactionService.getAccount((long) transaction.getReceiverId());
//            if(fromAccountOptional.isEmpty()) {
//                return new ResponseEntity("Origin Account does not exists", HttpStatus.BAD_REQUEST);
//            }
//
//            if(toAccountOptional.isEmpty()) {
//                return new ResponseEntity("Destiny Account does not exists", HttpStatus.BAD_REQUEST);
//            }

//
//            Account fromAccount = fromAccountOptional.get();
//            Account toAccount = toAccountOptional.get();

//            if(fromAccount.getBalance() < transaction.getAmountOfMoney()) {
//                return new ResponseEntity("Not enough money", HttpStatus.BAD_REQUEST);
//            }
//
//            fromAccount.setBalance(fromAccount.getBalance()-transaction.getAmountOfMoney());
//            toAccount.setBalance(toAccount.getBalance() + transaction.getAmountOfMoney());
//
//            transactionService.updateAccount(fromAccount);
//            transactionService.updateAccount(toAccount);

            return ResponseEntity.status(HttpStatus.OK).body(transactionService.createTransaction(transaction));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/lastTransactions/{userId}")
    public ResponseEntity<List<Transaction>> getLastFiveTransactions(@PathVariable Long userId) throws ResourceNotFoundException {
        Optional<List<Transaction>> optionalTransactions = transactionService.getLastFiveTransactionsByUserId(userId);
        if(optionalTransactions.isPresent()){
            return ResponseEntity.ok().body(optionalTransactions.get());
        }
        return (ResponseEntity<List<Transaction>>) ResponseEntity.notFound();

    }


    @GetMapping("/getAll/{userId}")
    public ResponseEntity<List<Transaction>> getAllTransactions(@PathVariable Long userId) throws ResourceNotFoundException {
        Optional<List<Transaction>> transactionsOptional = transactionService.getAllTransactions(userId);
        return ResponseEntity.status(HttpStatus.OK).body(transactionsOptional.get());
    }

    @GetMapping("/{transactionId}/account/{accountId}")
    public ResponseEntity<?> getTransaction(@PathVariable Long accountId, @PathVariable Long transactionId) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionById(accountId, transactionId));

    }

}
