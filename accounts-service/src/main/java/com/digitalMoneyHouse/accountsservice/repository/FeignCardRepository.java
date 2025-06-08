package com.digitalMoneyHouse.accountsservice.repository;

import com.digitalMoneyHouse.accountsservice.entities.Card;
import com.digitalMoneyHouse.accountsservice.feignCustomExceptions.CustomErrorDecoder;
import com.digitalMoneyHouse.accountsservice.feignCustomExceptions.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "cards-service", url = "http://cards-service:8085/card", configuration = {FeignConfig.class, CustomErrorDecoder.class})
public interface FeignCardRepository {

    @PostMapping("/register-card")
    Card registerCard(@RequestBody Card card);

    @GetMapping("/{id}/all-cards")
    List<Card> getAllCardsByAccountId(@PathVariable Long id);

    @GetMapping("/{accountId}/card/{cardId}")
    Card getCardByIdAndAccountId (@PathVariable Long accountId, @PathVariable Long cardId);

    @DeleteMapping("/{accountId}/card/{cardNumber}")
    void deleteCard(@PathVariable Long accountId, @PathVariable String cardNumber);

    @GetMapping("/{accountId}/cardNumber/{cardNumber}")
    Card getCardByNumberAndAccountId (@PathVariable Long accountId, @PathVariable String cardNumber);

}
