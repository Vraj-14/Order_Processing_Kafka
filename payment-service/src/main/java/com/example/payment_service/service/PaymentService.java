package com.example.payment_service.service;

import com.example.common.dto.InventoryResultEvent;
import com.example.common.dto.OrderEvent;
import com.example.common.dto.PaymentResultEvent;
import com.example.common.enums.EventType;
import com.example.common.enums.InventoryStatus;
import com.example.common.enums.PaymentStatus;
import com.example.payment_service.document.PaymentDocument;
import com.example.payment_service.repository.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Data
@Slf4j
public class PaymentService {

    private final ObjectMapper objectMapper;


    @Autowired
    PaymentRepository paymentRepo;

    public PaymentResultEvent processPayment(InventoryResultEvent inventoryResultEvent) {

        PaymentDocument paymentDocument = new PaymentDocument();
        paymentDocument.setOrderId(inventoryResultEvent.getOrderId());

        try{

            if (inventoryResultEvent.getStatus() == InventoryStatus.INVENTORY_RESERVED){

                BigDecimal totalAmount = inventoryResultEvent.getUnitPrice()
                        .multiply(BigDecimal.valueOf(inventoryResultEvent.getQuantity()));

                paymentDocument.setTotalAmount(totalAmount);
                paymentDocument.setStatus(PaymentStatus.PAYMENT_SUCCESS);

                paymentRepo.save(paymentDocument);

                log.info("[PAYMENT] Success for order: {} | Total: {}",
                        inventoryResultEvent.getOrderId(),
                        totalAmount);

                return new PaymentResultEvent(
                        inventoryResultEvent.getOrderId(), totalAmount, PaymentStatus.PAYMENT_SUCCESS
                );

            } else {
                paymentDocument.setTotalAmount(BigDecimal.ZERO);
                paymentDocument.setStatus(PaymentStatus.PAYMENT_FAILED);

                paymentRepo.save(paymentDocument);

                log.warn("[PAYMENT] Failed for order: {} | reason: {}",
                        inventoryResultEvent.getOrderId(), inventoryResultEvent.getReason());

                return new PaymentResultEvent(
                        inventoryResultEvent.getOrderId(), BigDecimal.ZERO,
                        PaymentStatus.PAYMENT_FAILED
                );
            }


        }
        catch (Exception e){
            log.info("Exception occurred in payment service");

            return new PaymentResultEvent(
                    inventoryResultEvent.getOrderId(), BigDecimal.ZERO,
                    PaymentStatus.PAYMENT_FAILED
            );
        }
    }
}
