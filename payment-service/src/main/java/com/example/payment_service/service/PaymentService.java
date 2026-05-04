package com.example.payment_service.service;

import com.example.common.dto.OrderEvent;
import com.example.common.enums.EventType;
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

    BigDecimal amount;
    int quantity;
    BigDecimal totalAmount;

    @Autowired
    PaymentRepository paymentRepo;

    public BigDecimal processPayment(String message) {

        try{
            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);

            amount = orderEvent.getAmount();
            quantity = orderEvent.getQuantity();

            PaymentDocument paymentDocument = new PaymentDocument();

            paymentDocument.setOrderId(orderEvent.getOrderId());
            paymentDocument.setAmount(orderEvent.getAmount());
            paymentDocument.setStatus(EventType.ORDER_CREATED);

            paymentRepo.save(paymentDocument);
            log.info("Payment saved to MongoDB for order: {}", orderEvent.getOrderId());

            return totalAmount = amount.multiply(BigDecimal.valueOf(quantity));

        }
        catch (Exception e){
            log.info("Exception occurred in payment service");
        }
        return  totalAmount;
    }
}
