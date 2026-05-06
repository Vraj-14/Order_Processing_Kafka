package com.example.inventory_service.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "product_stock")
@Data
public class ProductStockDocument {

    @Id
    private String id;
    private String product;
    private Integer totalStock;
    private Integer availableStock;

}
