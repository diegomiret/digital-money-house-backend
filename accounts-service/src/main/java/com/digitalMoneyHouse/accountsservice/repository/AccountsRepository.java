package com.digitalMoneyHouse.accountsservice.repository;

import com.digitalMoneyHouse.accountsservice.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId(Long userId);

    List<Account> findAllByUserId(Long userId);
}
