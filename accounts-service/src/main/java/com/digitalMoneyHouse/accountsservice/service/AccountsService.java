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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    public List<TransactionDTO> getLastFiveTransactionsFull(Long userId) throws ResourceNotFoundException {
        List<Transaction> transactions = feignTransactionRepository.getLastFiveTransactions(userId);
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found");
        }

        User feignUser = feignUserRepository.getOriginalUserById(userId);
        //User feignUser = feignUserRepository.getOriginalUserById(userId);

        return transactions.stream()
                .map(transaction -> {
                    TransactionDTO dto = new TransactionDTO();
                    dto.setId(transaction.getId().toString());
                    dto.setAmount(transaction.getAmountOfMoney());
                    dto.setOrigin(String.valueOf(transaction.getSenderId()));
                    dto.setDestination(String.valueOf(transaction.getReceiverId()));
                    dto.setDated(transaction.getDate().atZone(ZoneId.systemDefault()));

                    boolean isTransfer = transaction.getSenderId() != transaction.getReceiverId();
                    boolean isDeposit = transaction.getSenderId() == userId.intValue()
                            && transaction.getReceiverId() == userId.intValue();


                            long idUserAmostrar = 0;

                            if(isTransfer){
                        //  si es transferencia, coloco el nombre dependendiendo el destinatario
                        if(transaction.getAmountOfMoney() < 0){
                            //  es recibida, entoces muestro el que elnvio
                            idUserAmostrar = transaction.getReceiverId();
                        }
                        if(transaction.getAmountOfMoney() > 0) {
                            //  es recibida, entoces muestro el que elnvio
                            idUserAmostrar = transaction.getSenderId();
                        }
                        }

                    if (isTransfer){
                        User feignUserSearched = feignUserRepository.getOriginalUserById(idUserAmostrar);

                        dto.setName(feignUserSearched.getFirstName() + " " + feignUserSearched.getLastName());

                    }else{
                        dto.setName("");
                    }


                    dto.setType(isTransfer
                            ? TransactionType.Transfer
                            : isDeposit
                            ? TransactionType.Deposit
                            : null); // O manejar otro caso si aplica

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Transaction> getAllTransactions(Long userId) throws ResourceNotFoundException {
        List<Transaction> transactions = feignTransactionRepository.getAllTransactions(userId);
        if(transactions.isEmpty()){
            throw new ResourceNotFoundException("No transactions found");
        }
        return transactions;
    }

    public ActivityDTOResponse getTransaction(Long userId, Long transactionId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();

            Transaction transaccion = feignTransactionRepository.getTransaction(account.getId(), transactionId);

            ActivityDTOResponse response = new ActivityDTOResponse();
            response.setAmount(transaccion.getAmountOfMoney());
            response.setType(transaccion.getType());

            return response;

        }
    }

    public Card registerCard(@RequestBody CardRequest card, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            Card newCard = new Card(account.getId(), card.getName(), card.getNumber(), card.getExpiration(), card.getCvc());
            return feignCardRepository.registerCard(newCard);
        }
    }

    public List<CardResponseDTO> getAllCards(Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            List<Card> allCards =  feignCardRepository.getAllCardsByAccountId(account.getId());

            List<CardResponseDTO> dtos = new ArrayList<>();

            for (Card card : allCards) {
                String type;
                String number = card.getNumber();

                if (number == null || number.isEmpty()) {
                    type = "UNKNOWN";
                } else if (number.startsWith("4")) {
                    type = "VISA";
                } else if (number.startsWith("5")) {
                    type = "MASTERCARD";
                } else if (number.startsWith("3")) {
                    type = "AMEX";
                } else {
                    type = "OTHER";
                }

                CardResponseDTO dto = new CardResponseDTO(
                        card.getId().toString(), // No tenés ID en la entidad Card
                        card.getNumber(),
                        card.getName(),
                        type
                );

                dtos.add(dto);
            }

            return dtos;




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
        System.out.println(card.getNumber() + " --- " + card.getName());
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


    public Account patchAccount(Long userId, PatchAccountRequest request) throws ResourceNotFoundException {

        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);

        if (accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        }
        Account account = accountOptional.get();

        // Validar que la cuenta pertenezca al usuario
        if (!account.getUserId().equals(userId)) {
            throw new ResourceNotFoundException("Account does not belong to the user");
        }

        // Aplicar los cambios si fueron enviados
        if (request.getAlias() != null) {
            account.setAlias(request.getAlias());
        }

        return accountsRepository.save(account);
    }

    public void deleteCardById(Long idCard, Long userId) throws ResourceNotFoundException {
        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            Account account = accountOptional.get();
            feignCardRepository.deleteCardById(account.getId(), idCard);
        }
    }

    public Transaction addActivity(ActivityRequestDTO request, Long userId) throws ResourceNotFoundException {
        //Card card = getCardByNumber(request.getCardNumber(), userId);

        Optional<Account> accountOptional = accountsRepository.findByUserId(userId);
        if(accountOptional.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        } else {
            //Account account = accountOptional.get();

            CreateTransaction newTransaction = new CreateTransaction();
            newTransaction.setType(request.getType());
            newTransaction.setDate(LocalDateTime.now());
            newTransaction.setDescription(request.getDescription());
            newTransaction.setReceiverId(Math.toIntExact(userId));
            newTransaction.setSenderId(Math.toIntExact(userId));
            newTransaction.setAmountOfMoney(request.getAmount());

            return feignTransactionRepository.createTransaction(newTransaction);

        }
    }
}
