package com.digitalMoneyHouse.transactionsservice.feignCustomExceptions;

import feign.codec.ErrorDecoder;

public class FeignConfig {

    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
