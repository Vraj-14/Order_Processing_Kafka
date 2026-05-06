package com.example.inventory_service.service;

import com.example.common.dto.OrderEvent;
import com.example.common.enums.EventType;
import com.example.inventory_service.document.InventoryDocument;
import com.example.inventory_service.document.ProductStockDocument;
import com.example.inventory_service.repository.InventoryRepository;
import com.example.inventory_service.repository.ProductStockRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class InventoryService {


    private final ObjectMapper objectMapper;

    // main inventory repo
    @Autowired
    InventoryRepository inventoryRepo;

    // repo for adding stock
    @Autowired
    ProductStockRepository productStockRepo;

    public void processInventory(String message) {

        try {

            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);

            // Look up current stock for this product
            ProductStockDocument stockDocument = productStockRepo
                    .findByProduct(orderEvent.getProduct())
                    .orElseThrow( () -> new RuntimeException(
                            "No stock record for product: " + orderEvent.getProduct()
                            )
                    );

            int available = stockDocument.getAvailableStock();
            int ordered = orderEvent.getQuantity();

            if (ordered > available){
                log.warn("Insufficient stock for {}. Available: {}, Ordered: {}",
                orderEvent.getProduct(), available, ordered);

                return;
            }

            // deduct and save back
            Integer remainingQuantity = available - ordered;
            stockDocument.setAvailableStock(remainingQuantity);    // update remainingStock in stock repo
            productStockRepo.save(stockDocument);                  // save to stock repo


            // save to main repo
            InventoryDocument inventoryDocument = new InventoryDocument();

            inventoryDocument.setOrderId(orderEvent.getOrderId());
            inventoryDocument.setProduct(orderEvent.getProduct());
            inventoryDocument.setQuantity(orderEvent.getQuantity());
            inventoryDocument.setRemainingQuantity(remainingQuantity);
            inventoryDocument.setStatus(EventType.ORDER_CREATED);

            inventoryRepo.save(inventoryDocument);

            log.info("Inventory saved to MongoDB for order: {}", orderEvent.getOrderId());
            log.info("Ordered Quantity : {}",orderEvent.getQuantity());
            log.info("Remaining inventory stock : {}",remainingQuantity);

        } catch (Exception e){
            log.info("Exception in inventory service",e);
        }

    }
}
