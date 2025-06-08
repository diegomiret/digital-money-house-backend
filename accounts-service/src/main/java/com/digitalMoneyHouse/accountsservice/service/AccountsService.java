package com.digitalMoneyHouse.accountsservice.service;

import com.digitalMoneyHouse.accountsservice.entities.*;
import com.digitalMoneyHouse.accountsservice.exceptions.BadRequestException;
import com.digitalMoneyHouse.accountsservice.exceptions.ResourceNotFoundException;
import com.digitalMoneyHouse.accountsservice.repository.AccountsRepository;
import com.digitalMoneyHouse.accountsservice.repository.FeignCardRepository;
import com.digitalMoneyHouse.accountsservice.repository.FeignTransactionRepository;
import com.digitalMoneyHouse.accountsservice.repository.FeignUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountsService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private FeignUserRepository feignUserRepository;

    @Autowired
    private FeignTransactionRepository feignTransactionRepository;

    @Autowired
    private FeignCardRepository feignCardRepository;

    public void createAccount(AccountRequest accountRequest) {
        Account account = new Account(accountRequest.getUserId(), accountRequest.getAlias(), accountRequest.getCvu());
        accountsRepository.save(account);
    }

    public AccountInformation getAccountInformation(Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isPresent()) {
            Account accountFound = accountOptional.get();
            User feignUser = feignUserRepository.getUserById(userId);
            return new AccountInformation(accountFound.getId(), accountFound.getUserId() ,accountFound.getBalance(), feignUser.getCvu(), feignUser.getAlias());
        } else {
            throw new ResourceNotFoundException("Account not found");
        }
    }

    public Account updateAccount(Account account) {
        return accountsRepository.save(account);
    }

    public List<Transaction> getLastFiveTransactions(Long userId) throws ResourceNotFoundException {
         List<Transaction> transactions = feignTransactionRepository.getLastFiveTransactions(userId);
         if(transactions.isEmpty()){
             throw new ResourceNotFoundException("No transactions found");
         }
         return transactions;
    }

    public List<Transaction> getAllTransactions(Long userId) throws ResourceNotFoundException {
        List<Transaction> transactions = feignTransactionRepository.getAllTransactions(userId);
        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("No transactions found");
        }
        return transactions;
    }

    public Transaction getTransaction(Long userId, Long transactionId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            return feignTransactionRepository.getTransaction(account.getId(), transactionId);
        }
    }

    public Card registerCard(@RequestBody CardRequest card, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            Card newCard = new Card(account.getId(), card.getHolder(), card.getNumber(), card.getExpirationDate(), card.getCvv());
            return feignCardRepository.registerCard(newCard);
        }
    }

    public List<Card> getAllCards(Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            return feignCardRepository.getAllCardsByAccountId(account.getId());
        }
    }


    public Card getCardById(Long cardId, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            return feignCardRepository.getCardByIdAndAccountId(account.getId(), cardId);
        }
    }

    public Card getCardByNumber(String cardNumber, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            return feignCardRepository.getCardByNumberAndAccountId(account.getId(), cardNumber);
        }
    }

    public void deleteCardByNumber(String cardNumber, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            feignCardRepository.deleteCard(account.getId(), cardNumber);
        }
    }

    public Long getUserIdByKcId(String kcId) {
        Long userId = feignUserRepository.getUserByKeycloakId(kcId);
        return userId;
    }

    public void addMoney(DepositMoneyRequest request, Long userId) throws ResourceNotFoundException {
            Card card = getCardByNumber(request.getCardNumber(), userId);
        System.out.println(card.getNumber() + " --- " + card.getHolder());
            Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
            if(accountOptional.isEmpty()) {
                throw new ResourceNotFoundException("Account not found");
            } else {
                Account account = accountOptional.get();
                account.setBalance(account.getBalance() + request.getAmount());
                accountsRepository.save(account);
            }
    }

    public Transaction sendMoney(TransactionRequest transactionRequest, Long originUserId) throws BadRequestException {
        String aliasPattern = "\\b(?:[a-zA-Z]+\\.?)+\\b";
        String cvuPattern = "[0-9]+";

        checkTransactionData(transactionRequest);
        int destinyUserId;

        if(transactionRequest.getDestinyAccount().matches(aliasPattern)) {
            destinyUserId = Math.toIntExact(feignUserRepository.getUserIdByAlias(transactionRequest.getDestinyAccount()));
        } else if (transactionRequest.getDestinyAccount().matches(cvuPattern) && transactionRequest.getDestinyAccount().length()==22) {
            destinyUserId = Math.toIntExact(feignUserRepository.getUserIdByCvu(transactionRequest.getDestinyAccount()));
            } else
            {
                throw new BadRequestException("Invalid destiny account");
            }

        return feignTransactionRepository.createTransaction(new CreateTransaction(Math.toIntExact(originUserId), destinyUserId, transactionRequest.getAmount(), LocalDateTime.now()));
    }

    private void checkTransactionData(TransactionRequest transactionRequest) throws BadRequestException {
        if(transactionRequest.getDestinyAccount().equals("") || transactionRequest.getDestinyAccount()==null) {
            throw new BadRequestException("No destiny account added");
        }
        if(transactionRequest.getAmount() == 0.0 || transactionRequest.getAmount()==null) {
            throw new BadRequestException("No amount added");
        }
    }

    public List<Account> getAccountsByIdUser(Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        List<Account> accounts = accountsRepository.findAllByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            return accounts;
        }
    }

}
