package com.example.payment_service.repository;

import com.example.payment_service.document.PaymentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<PaymentDocument,String> {
}
