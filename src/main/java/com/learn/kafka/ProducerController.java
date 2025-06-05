package com.learn.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.producer.MessageProducer;
import com.learn.kafka.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/connect-exchange")
    public ResponseEntity<String> sendMessage(@RequestParam String content) throws JsonProcessingException {

        var data = exchangeService.getExchangeRates(content);

        String jsonMessage = objectMapper.writeValueAsString(data);
        messageProducer.sendMessage("mon-tunnel-topic", jsonMessage);

        return ResponseEntity.ok(content);
    }

}