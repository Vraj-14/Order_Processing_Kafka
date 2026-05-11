package com.example.inventory_service.service;

import com.example.common.dto.InventoryResultEvent;
import com.example.common.dto.OrderEvent;
import com.example.common.enums.EventType;
import com.example.common.enums.InventoryStatus;
import com.example.inventory_service.document.InventoryDocument;
import com.example.inventory_service.document.ProductStockDocument;
import com.example.inventory_service.repository.InventoryRepository;
import com.example.inventory_service.repository.ProductStockRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

    public InventoryResultEvent processInventory(String message) {

        try {

            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);

            // Look up current stock for this product
            ProductStockDocument stockDocument = productStockRepo
                    .findByProduct(orderEvent.getProduct())
                    .orElse(null);


            // product dont exist
            if (stockDocument == null) {
                log.warn("[INVENTORY] No stock record for product: {}", orderEvent.getProduct());

                return new InventoryResultEvent(
                        orderEvent.getOrderId(),
                        orderEvent.getProduct(),
                        orderEvent.getQuantity(),
                        orderEvent.getAmount(),
                        InventoryStatus.INVENTORY_FAILED,
                        "Product Not Found : " + orderEvent.getProduct()
                );
            }

            // insufficient quantity
            if (orderEvent.getQuantity() > stockDocument.getAvailableStock()) {
                log.warn("[INVENTORY] Insufficient stock. Available: {}, Ordered: {}",
                        stockDocument.getAvailableStock(), orderEvent.getQuantity());

                return new InventoryResultEvent(
                        orderEvent.getOrderId(),
                        orderEvent.getProduct(),
                        orderEvent.getQuantity(),
                        orderEvent.getAmount(),
                        InventoryStatus.INVENTORY_FAILED,
                        "Insufficient Stock : " + orderEvent.getQuantity()
                );
            }

            // happy-path

            // Deduct stock
            stockDocument.setAvailableStock(stockDocument.getAvailableStock() - orderEvent.getQuantity());
            // save to admin repo
            productStockRepo.save(stockDocument);

            // Save inventory record
            InventoryDocument doc = new InventoryDocument();
            doc.setOrderId(orderEvent.getOrderId());
            doc.setProduct(orderEvent.getProduct());
            doc.setQuantity(orderEvent.getQuantity());
            doc.setRemainingQuantity(stockDocument.getAvailableStock());
            doc.setStatus(EventType.ORDER_CREATED);
            inventoryRepo.save(doc);

            log.info("[INVENTORY] Reserved {} units of {} for order: {}",
                    orderEvent.getQuantity(), orderEvent.getProduct(), orderEvent.getOrderId());


            return new InventoryResultEvent(
                    orderEvent.getOrderId(),
                    orderEvent.getProduct(),
                    orderEvent.getQuantity(),
                    orderEvent.getAmount(),
                    InventoryStatus.INVENTORY_RESERVED,
                    "OK"
            );


        } catch (Exception e) {
            log.error("[INVENTORY] Exception in processInventory", e);
            return null;

        }
    }
}
