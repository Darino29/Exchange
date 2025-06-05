package com.learn.kafka.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ExchangeService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getExchangeRates(String baseCurrency) {
        String url = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
        return restTemplate.getForObject(url, Map.class);
    }
}
