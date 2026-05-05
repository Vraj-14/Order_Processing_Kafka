package com.example.payment_service.consume;

import com.example.common.constants.KafkaTopics;
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

    private BigDecimal totalAmount;

    public PaymentEventConsumer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        log.info("PaymentEventConsumer initialized with ObjectMapper");
    }

    @KafkaListener(
            topics = KafkaTopics.ORDER_EVENTS,
            groupId = "payment-group"
    )
    public void consume(String message){
        log.info("[PAYMENT] Received message from Kafka: {}", message);

        try {
            log.debug("[PAYMENT] Attempting to deserialize message");

            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);

            log.info("[PAYMENT] Successfully deserialized OrderEvent with ID: {}", orderEvent.getEventId());

            if (orderEvent.getEventType() == EventType.ORDER_CREATED){
                log.info("[PAYMENT] Processing payment for order: {},quantity:{}, amount: {}",
                        orderEvent.getOrderId(),
                        orderEvent.getQuantity(),
                        orderEvent.getAmount());

                totalAmount = paymentService.processPayment(message);

                log.info(String.valueOf(totalAmount));
            } else {
                log.info("[PAYMENT] Event type is not ORDER_CREATED, skipping. Event type: {}", orderEvent.getEventType());
            }

        } catch (Exception e){
            log.error("[PAYMENT] Error processing message: {}", message, e);
            e.printStackTrace();
        }


    }

}