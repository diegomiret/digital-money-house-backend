package com.digitalMoneyHouse.cardsservice.service;

import com.digitalMoneyHouse.cardsservice.entities.Card;
import com.digitalMoneyHouse.cardsservice.exceptions.BadRequestException;
import com.digitalMoneyHouse.cardsservice.exceptions.ConflictException;
import com.digitalMoneyHouse.cardsservice.exceptions.ResourceNotFoundException;
import com.digitalMoneyHouse.cardsservice.repository.CardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    public Card registerCard(Card card) throws ConflictException, BadRequestException {
        checkCardData(card);
        Optional<Card> cardOptional = cardRepository.findByNumber(card.getNumber());
        if(cardOptional.isPresent()) {
            throw new ConflictException("Card already exists");
        } else {
            return cardRepository.save(card);
        }
    }

    private void checkCardData(Card card) throws BadRequestException {
        if(card.getAccountId() == null) {
            throw new BadRequestException("No account associated");
        }
        if(card.getName()==null || card.getName().equals("")) {
            throw new BadRequestException("Holder missing");
        }
        if(card.getNumber() == null || card.getNumber().length()!=16) {
            throw new BadRequestException("Number missing");
        }
        if(card.getExpiration() == null) {
            throw new BadRequestException("Expiration date missing");
        }
//        if(card.getExpirationDate().isBefore(LocalDate.now())) {
//            throw new BadRequestException("Card already expired");
//        }
        if(card.getCvc() == null || card.getCvc().equals("")) {
            throw new BadRequestException("CVV missing");
        }
    }

    public Card getCardByIdAndAccountId(Long cardId, Long accountId) throws ResourceNotFoundException {
        Optional<Card> cardOptional = cardRepository.findByIdAndAccountId(cardId, accountId);

        if(cardOptional.isEmpty()) {
            throw new ResourceNotFoundException("Card not found");
        } else {
            return cardOptional.get();
        }
    }

    public Card getCardByNumberAndAccountId(String cardNumber, Long accountId) throws ResourceNotFoundException {
        Optional<Card> cardOptional = cardRepository.findByNumberAndAccountId(cardNumber, accountId);
        if(cardOptional.isEmpty()) {
            throw new ResourceNotFoundException("Card not found");
        } else {
            return cardOptional.get();
        }
    }

    public List<Card> getAllCardsByAccountId(Long accountId) throws ResourceNotFoundException {
        Optional<List<Card>> optionalCardList = cardRepository.findAllByAccountId(accountId);
        if (optionalCardList.isEmpty()) {
            throw new ResourceNotFoundException("No card list found");
        }
        return optionalCardList.get();
    }
    @Transactional
    public void deleteCard(String cardNumber, Long accountId) throws ResourceNotFoundException {
        Optional<Card> optionalCard = cardRepository.findByNumberAndAccountId(cardNumber, accountId);
        if(optionalCard.isEmpty()) {
            throw new ResourceNotFoundException("Card not found");
        } else {
            Card cardFound = optionalCard.get();
            cardRepository.deleteByIdAndAccountId(cardFound.getId(), accountId);
        }
    }

    @Transactional
    public void deleteCardByIds(Long idAccount, Long idCard) throws ResourceNotFoundException {
        Optional<Card> optionalCard = cardRepository.findByAccountIdAndId(idAccount, idCard);
        if(optionalCard.isEmpty()) {
            throw new ResourceNotFoundException("Card not found");
        } else {
            Card cardFound = optionalCard.get();
            cardRepository.deleteByIdAndAccountId(cardFound.getId(), idAccount);
        }
    }

}
