package com.example.inventory_service.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
public class AddStockRequest {

    // for adding stock

    private String product;
    private Integer quantity;
    private BigDecimal unitPrice;

}
