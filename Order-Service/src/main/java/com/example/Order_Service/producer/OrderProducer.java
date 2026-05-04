package com.example.Order_Service.producer;

import com.example.common.constants.KafkaTopics;
import com.example.common.dto.OrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
//import tools.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate,
                         ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(OrderEvent event) {
        log.info("order sent to producer with order ID: {}", event.getOrderId());
        try {
            String json = objectMapper.writeValueAsString(event);
            log.info("Serialized JSON: {}", json);

            kafkaTemplate.send(
                    KafkaTopics.ORDER_EVENTS,
                    event.getOrderId(),
                    json
            ).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to send message", ex);
                } else {
                    log.info("Message sent successfully to topic: {}, partition: {}, offset: {}",
                            KafkaTopics.ORDER_EVENTS,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                }
            });

        } catch (Exception e) {
            log.error("Error serializing order event", e);
        }
    }
}