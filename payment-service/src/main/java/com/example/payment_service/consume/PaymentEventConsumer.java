package com.example.payment_service.consume;

import com.example.common.constants.KafkaTopics;
import com.example.common.dto.InventoryResultEvent;
import com.example.common.dto.OrderEvent;
import com.example.common.enums.EventType;
import com.example.payment_service.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class PaymentEventConsumer {

    private final ObjectMapper objectMapper;

    @Autowired
    PaymentService paymentService;


    public PaymentEventConsumer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        log.info("PaymentEventConsumer initialized with ObjectMapper");
    }

    @KafkaListener(
            topics = KafkaTopics.INVENTORY_RESULT,
            groupId = "payment-group"
    )
    public void consume(String message){
        log.info("[PAYMENT] Received inventory result: {}", message);

        try {
            log.debug("[PAYMENT] Attempting to deserialize message");

            InventoryResultEvent inventoryResultEvent = objectMapper.readValue(message, InventoryResultEvent.class);

            paymentService.processPayment(inventoryResultEvent);

        } catch (Exception e){
            log.error("[PAYMENT] Error processing inventory result: {}", message, e);

        }


    }

}