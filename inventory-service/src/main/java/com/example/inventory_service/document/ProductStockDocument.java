package com.example.inventory_service.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

// Admin side - for adding stock
@Document(collection = "product_stock")
@Data
public class ProductStockDocument {

    @Id
    private String id;
    private String product;
    private BigDecimal unitPrice;
    private Integer availableStock;

}
