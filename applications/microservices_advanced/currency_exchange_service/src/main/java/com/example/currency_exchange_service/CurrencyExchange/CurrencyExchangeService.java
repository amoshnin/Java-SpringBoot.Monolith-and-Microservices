package com.example.currency_exchange_service.CurrencyExchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeService {
    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    public CurrencyExchange retrieveExchangeValue(String from, String to) {
        return this.currencyExchangeRepository.findByFromAndTo(from, to);
    }
}
