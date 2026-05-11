package com.example.inventory_service.repository;

import com.example.inventory_service.document.ProductStockDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

// repo for adding and deducting stock
// admin side repo
public interface ProductStockRepository extends MongoRepository<ProductStockDocument,String> {

    Optional<ProductStockDocument> findByProduct(String product);

}
