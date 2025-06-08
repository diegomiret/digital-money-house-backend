package com.dh.digitalMoneyHouse.usersservice.repository;

import com.dh.digitalMoneyHouse.usersservice.entities.AccountRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "accounts-service", url = "http://localhost:8082/account")
public interface FeignAccountRepository {

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest accountRequest);
}
