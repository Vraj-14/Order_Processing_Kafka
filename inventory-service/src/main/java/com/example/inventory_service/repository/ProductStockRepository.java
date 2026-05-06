package com.example.inventory_service.repository;

import com.example.inventory_service.document.ProductStockDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductStockRepository extends MongoRepository<ProductStockDocument,String> {

    Optional<ProductStockDocument> findByProduct(String product);
}
