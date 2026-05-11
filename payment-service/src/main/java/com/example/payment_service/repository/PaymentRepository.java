package com.example.payment_service.repository;

import com.example.payment_service.document.PaymentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentDocument,String> {
}
