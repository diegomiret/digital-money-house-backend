package com.digitalMoneyHouse.transactionsservice.service;

import com.digitalMoneyHouse.transactionsservice.entities.Account;
import com.digitalMoneyHouse.transactionsservice.entities.Transaction;
import com.digitalMoneyHouse.transactionsservice.entities.TransactionRequest;
import com.digitalMoneyHouse.transactionsservice.exceptions.ResourceNotFoundException;
import com.digitalMoneyHouse.transactionsservice.repository.FeignAccountRepository;
import com.digitalMoneyHouse.transactionsservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private FeignAccountRepository feignAccountRepository;
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, FeignAccountRepository feignAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.feignAccountRepository = feignAccountRepository;
    }

    public Transaction createTransaction(TransactionRequest transaction) {

        Transaction newTransaction = new Transaction();
        newTransaction.setDate(transaction.getDate());
        newTransaction.setDescription(transaction.getDescription());
        newTransaction.setType(transaction.getType());
        newTransaction.setReceiverId(transaction.getReceiverId());
        newTransaction.setSenderId(transaction.getSenderId());
        newTransaction.setAmountOfMoney(transaction.getAmountOfMoney());


        return transactionRepository.save(newTransaction);
    }

    public Optional<Account> getAccount(Long userId) {
        try{
            Account account = feignAccountRepository.getAccountById(userId);
            return Optional.of(account);
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    public Optional<List<Transaction>> getLastFiveTransactionsByUserId(Long userId) throws ResourceNotFoundException {
        List<Transaction> lastFiveTransactions = transactionRepository.getLastFiveTransactionsByUserId(userId, Pageable.ofSize(5));
        if(lastFiveTransactions==null || lastFiveTransactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found");
        }
        return Optional.of(lastFiveTransactions);
    }

    public Optional<List<Transaction>> getAllTransactions(Long userId) throws ResourceNotFoundException {
        List<Transaction> transactions = transactionRepository.getAllTransactionsById(userId);
        if(transactions==null) {
            throw new ResourceNotFoundException("No transactions found");
        }
        return Optional.of(transactions);
    }


    public void updateAccount(Account account) {
        feignAccountRepository.updateBalance(account, account.getId());
    }

    public Transaction getTransactionById(Long accountId, Long transactionId) throws ResourceNotFoundException {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if(transactionOptional.isEmpty()) {
            throw new ResourceNotFoundException("No transaction was found with the provided id");
        } else {
            return transactionOptional.get();
        }
    }

}
