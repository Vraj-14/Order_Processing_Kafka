package com.example.inventory_service.consume;

import com.example.common.constants.KafkaTopics;
import com.example.common.dto.OrderEvent;
import com.example.common.enums.EventType;
import com.example.inventory_service.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
//import tools.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventConsumer {

    private final ObjectMapper objectMapper;

    @Autowired
    InventoryService inventoryService;

    public InventoryEventConsumer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = KafkaTopics.ORDER_EVENTS,
            groupId = "inventory-group"
    )
    public void consume(String message){

        try {
            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);

            if (orderEvent.getEventType() == EventType.ORDER_CREATED){

                System.out.println("[INVENTORY] Inventory received for " +
                        orderEvent.getOrderId());

                inventoryService.processInventory(message);

            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
