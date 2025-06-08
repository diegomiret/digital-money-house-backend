package com.digitalMoneyHouse.accountsservice.repository;

import com.digitalMoneyHouse.accountsservice.entities.CreateTransaction;
import com.digitalMoneyHouse.accountsservice.entities.Transaction;
import com.digitalMoneyHouse.accountsservice.feignCustomExceptions.CustomErrorDecoder;
import com.digitalMoneyHouse.accountsservice.feignCustomExceptions.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "transactions-service", url = "http://transactions-service:8083/transaction", configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface FeignTransactionRepository {

    @GetMapping("/lastTransactions/{userId}")
    List<Transaction> getLastFiveTransactions(@PathVariable Long userId);

    @GetMapping("/getAll/{userId}")
    List<Transaction> getAllTransactions(@PathVariable Long userId);

    @GetMapping("/{transactionId}/account/{accountId}")
    Transaction getTransaction(@PathVariable Long accountId, @PathVariable Long transactionId);

    @PostMapping("/create")
    Transaction createTransaction(@RequestBody CreateTransaction transaction);
}
