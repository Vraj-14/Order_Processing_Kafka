package com.example.inventory_service.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class AddStockRequest {

    private String product;
    private Integer quantity;

}
