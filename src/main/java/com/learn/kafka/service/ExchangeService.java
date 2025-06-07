package com.learn.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;

@Service
public class ExchangeService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> getExchangeRates(String baseCurrency) {
        String url = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
        return restTemplate.getForObject(url, Map.class);
    }

    @Scheduled(fixedRate = 60000) // exécute toutes les 60 secondes
    public void fetchAndSendRates() {
        try {
            Map<String, Object> data = getExchangeRates("USD");
            Map<String, Object> rawRates = (Map<String, Object>) data.get("rates");
            Map<String, Double> rates = new HashMap<>();
            for (Map.Entry<String, Object> entry : rawRates.entrySet()) {
                try {
                    rates.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
                } catch (Exception e) {
                    System.err.println("❌ Erreur de conversion pour " + entry.getKey() + " : " + entry.getValue());
                }
            }
            rates.replaceAll((k, v) -> {
                double variation = (Math.random() - 0.5) * 0.1; // ±5%
                return Math.round((v + (v * variation)) * 100000.0) / 100000.0;
            });
            data.put("rates", rates);
            String json = objectMapper.writeValueAsString(data);
            messageProducer.sendMessage("mon-tunnel-topic", json);
            System.out.println("📤 Données envoyées avec variation simulée ✅");
        } catch (JsonProcessingException e) {
            System.err.println("❌ Erreur lors de la sérialisation JSON : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l’appel de l’API : " + e.getMessage());
        }
    }
}
