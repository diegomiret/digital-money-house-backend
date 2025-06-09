package com.digitalMoneyHouse.cardsservice.repository;

import com.digitalMoneyHouse.cardsservice.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByIdAndAccountId(Long id, Long accountId);

    Optional<Card> findByNumberAndAccountId(String number, Long accountId);
    Optional<List<Card>> findAllByAccountId(Long accountId);
    void deleteByIdAndAccountId(Long id, Long accountId);


    Optional<Card> findByNumber(String number);

    Optional<Card> findByAccountIdAndId(Long accountId, Long id);
}
