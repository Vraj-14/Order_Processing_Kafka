package com.example.payment_service.document;

import com.example.common.enums.EventType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "payments")
public class PaymentDocument {

    @Id
    private String id;

    private String orderId;
    private BigDecimal amount;
    private EventType status;



}