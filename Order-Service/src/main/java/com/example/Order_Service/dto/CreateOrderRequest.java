package com.example.Order_Service.dto;

import lombok.*;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;

/** Belongs to API layer
 represents client input*/
@Data
@NoArgsConstructor
public class CreateOrderRequest {

    private String orderId;
    private String product;
    private int quantity;
    private BigDecimal amount;

}