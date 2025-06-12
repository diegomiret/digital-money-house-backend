package com.digitalMoneyHouse.accountsservice.feignCustomExceptions;

import com.digitalMoneyHouse.accountsservice.exceptions.BadRequestException;
import com.digitalMoneyHouse.accountsservice.exceptions.ConflictException;
import com.digitalMoneyHouse.accountsservice.exceptions.ResourceNotFoundException;
import com.digitalMoneyHouse.accountsservice.exceptions.TransactionGoneException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomErrorDecoder  implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400: return new BadRequestException("Bad request, check information");
            case 404: return new ResourceNotFoundException("Resource not found");
            case 409: return new ConflictException("Resource already exists");
            case 410: return new TransactionGoneException("Transaction no longer available (410)");
            default: return new Exception("Try again later");
        }

    }
}
