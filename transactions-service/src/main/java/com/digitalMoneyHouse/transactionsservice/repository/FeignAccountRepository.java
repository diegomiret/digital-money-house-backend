package com.digitalMoneyHouse.transactionsservice.repository;

import com.digitalMoneyHouse.transactionsservice.entities.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "accounts-service", url = "http://accounts-service:8082/account")
public interface FeignAccountRepository {

    @GetMapping("/{id}")
    Account getAccountById(@PathVariable(value = "id") Long id);

    @PutMapping("/{id}")
    Account updateBalance(@RequestBody Account account, @PathVariable(value = "id") Long id);
}
