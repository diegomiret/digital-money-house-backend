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
import java.util.Objects;
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


    public List<TransactionDTO> getLastFiveTransactionsFull(Long userId, Integer limit) throws ResourceNotFoundException {

        List<Transaction> transactions = new ArrayList<>();

        if(limit != null) {
            transactions = feignTransactionRepository.getLastFiveTransactions(userId);
            if (transactions.isEmpty()) {
                throw new ResourceNotFoundException("No transactions found");
            }
        }else{
            transactions = feignTransactionRepository.getAllTransactions(userId);
            if (transactions.isEmpty()) {
                throw new ResourceNotFoundException("No transactions found");
            }
        }


        User feignUser = feignUserRepository.getOriginalUserById(userId);
        //User feignUser = feignUserRepository.getOriginalUserById(userId);

        return transactions.stream()
                .map(transaction -> {
                    TransactionDTO dto = new TransactionDTO();
                    dto.setId(transaction.getId().toString());

                    long idUserAmostrar = 0;

                    //  determino si es una transferencia saliente para devolver el valor negativo
                    if (Objects.equals(transaction.getType(), "Transfer")){

                        //  si el que envia es el usuario que consulta, entonces es una transferencia saliente
                        if(transaction.getSenderId() == userId.intValue()){
                            dto.setAmount(-Math.abs(transaction.getAmountOfMoney()));

                            // si es una transferencia saliente,  el nombre a mostrar es el destinatario
                            idUserAmostrar = transaction.getReceiverId();

                        }
                        //  Si el que envia es otro, entonces es una transferencia entrante
                        else{
                            dto.setAmount(Math.abs(transaction.getAmountOfMoney()));

                            // si es una transferencia entrante,  el nombre a mostrar es emisor
                            idUserAmostrar = transaction.getSenderId();
                        }

                        //  Obtengo el nombre del destinatario o del emisor
                        User feignUserSearched = feignUserRepository.getOriginalUserById(idUserAmostrar);
                        dto.setName(feignUserSearched.getFirstName() + " " + feignUserSearched.getLastName());
                    }

                    //  Si es deposito, siempre devuelvo positivo
                    if (Objects.equals(transaction.getType(), "Deposit")) {
                        dto.setAmount(Math.abs(transaction.getAmountOfMoney()));

                        //  No tiene nombre si es un deposito
                        dto.setName("");
                    }

                    dto.setOrigin(String.valueOf(transaction.getSenderId()));
                    dto.setDestination(String.valueOf(transaction.getReceiverId()));
                    dto.setDated(transaction.getDate().atZone(ZoneId.systemDefault()));


                    dto.setType(TransactionType.valueOf(transaction.getType()));

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
            response.setDated(transaccion.getDate());
            response.setName(transaccion.getDescription());

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

            Long reciber = userId;
            if (Objects.equals(request.getType(), "Transfer")){
                Account account = accountsRepository.findByCvu(request.getDestination())
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No se encontró ninguna cuenta con ese CVU"));

                reciber = account.getUserId();
            }

            CreateTransaction newTransaction = new CreateTransaction();
            newTransaction.setType(request.getType());
            newTransaction.setDate(LocalDateTime.now());
            newTransaction.setDescription(request.getDescription());
            newTransaction.setReceiverId(Math.toIntExact(reciber));
            newTransaction.setSenderId(Math.toIntExact(userId));

            //  Guardo el valor absoluto
            newTransaction.setAmountOfMoney(Math.abs(request.getAmount()));

            return feignTransactionRepository.createTransaction(newTransaction);

        }
    }

    public List<AccountInformation> getAllAccounts() {

        List<Account> accounts = accountsRepository.findAll();

        List<AccountInformation> accountInfos = accounts.stream()
                .map(account -> new AccountInformation(
                        account.getId(),
                        account.getUserId(),
                        account.getBalance(),
                        account.getCvu(),
                        account.getAlias()
                ))
                .collect(Collectors.toList());

        return accountInfos;
    }
}
