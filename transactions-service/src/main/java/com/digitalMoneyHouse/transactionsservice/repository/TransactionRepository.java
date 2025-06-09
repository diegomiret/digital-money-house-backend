package com.digitalMoneyHouse.transactionsservice.repository;

import com.digitalMoneyHouse.transactionsservice.entities.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM transactions WHERE (sender_id= ?1 OR receiver_id= ?1) ORDER BY date DESC", nativeQuery = true)
    List<Transaction> getLastFiveTransactionsByUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT * FROM transactions WHERE (sender_id= ?1 OR receiver_id= ?1) ORDER BY date DESC", nativeQuery = true)
    List<Transaction> getAllTransactionsById(Long userId);

    @Query(value = "SELECT * FROM transactions WHERE (sender_id= ?1 OR receiver_id= ?1) AND id=?2 ORDER BY date DESC", nativeQuery = true)
    Optional<Transaction> findTransaction(Long accountId, Long transactionId);

}
