package com.example.inventory_service.service;

import com.example.common.dto.OrderEvent;
import com.example.common.enums.EventType;
import com.example.inventory_service.InventoryRepository;
import com.example.inventory_service.document.InventoryDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class InventoryService {


    private final ObjectMapper objectMapper;

    @Autowired
    InventoryRepository inventoryRepo;

    public void processInventory(String message) {

        try {

            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);

            InventoryDocument inventoryDocument = new InventoryDocument();

            inventoryDocument.setOrderId(orderEvent.getOrderId());
            inventoryDocument.setProduct(orderEvent.getProduct());
            inventoryDocument.setQuantity(orderEvent.getQuantity());
            inventoryDocument.setStatus(EventType.ORDER_CREATED);

            inventoryRepo.save(inventoryDocument);

            log.info("Inventory saved to MongoDB for order: {}", orderEvent.getOrderId());


        } catch (Exception e){
            log.info("Exception in inventory service");
        }

    }
}
