package com.example.inventory_service.repository;

import com.example.inventory_service.document.InventoryDocument;
import com.example.inventory_service.document.ProductStockDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InventoryRepository extends MongoRepository<InventoryDocument,String> {


}
