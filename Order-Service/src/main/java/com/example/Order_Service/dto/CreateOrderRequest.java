package com.example.Order_Service.dto;

import lombok.*;

import java.math.BigDecimal;

/** Belongs to API layer
 represents client input*/
@Data
@NoArgsConstructor
public class CreateOrderRequest {

    private String orderId;
    private String product;
    private int quantity;

}