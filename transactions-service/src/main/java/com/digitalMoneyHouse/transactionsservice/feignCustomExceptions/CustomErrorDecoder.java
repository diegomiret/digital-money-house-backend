package com.digitalMoneyHouse.transactionsservice.feignCustomExceptions;


import com.digitalMoneyHouse.transactionsservice.exceptions.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder  implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404: return new ResourceNotFoundException("Resource not found");
            default: return new Exception("Try again later");
        }

    }
}
