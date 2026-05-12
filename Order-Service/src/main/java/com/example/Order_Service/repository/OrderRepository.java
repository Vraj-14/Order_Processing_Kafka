package com.example.Order_Service.repository;

import com.example.Order_Service.document.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<OrderDocument, String> {

    Optional<OrderDocument> findByOrderId(String orderId);

}
