package com.digitalMoneyHouse.accountsservice.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 410) {
            return new TransactionGoneException("Transacción no disponible (HTTP 410)");
        }

        return defaultDecoder.decode(methodKey, response);
    }
}
