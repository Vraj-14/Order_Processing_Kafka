package com.example.inventory_service;

import com.example.inventory_service.document.InventoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<InventoryDocument,String> {
}
