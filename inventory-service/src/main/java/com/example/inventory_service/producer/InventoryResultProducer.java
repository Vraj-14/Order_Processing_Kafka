package com.example.inventory_service.producer;

import com.example.common.constants.KafkaTopics;
import com.example.common.dto.InventoryResultEvent;
import com.example.common.enums.InventoryStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class InventoryResultProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(InventoryResultEvent inventoryResultEvent){

        log.info("Sending inventory result event to payment service");

        try {

            String json = objectMapper.writeValueAsString(inventoryResultEvent);

            kafkaTemplate.send(
                    KafkaTopics.INVENTORY_RESULT,
                    inventoryResultEvent.getOrderId(),
                    json
            ).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("[INVENTORY] Failed to publish result event", ex);
                } else {
                    log.info("[INVENTORY] Published {} for order: {}",
                            inventoryResultEvent.getStatus(), inventoryResultEvent.getOrderId());
                }
            });

        } catch (Exception e) {
            log.error("[INVENTORY] Error serializing InventoryResultEvent", e);
        }

    }

}
