package com.example.Order_Service.service;

import com.example.Order_Service.document.OrderDocument;
import com.example.Order_Service.dto.CreateOrderRequest;
import com.example.Order_Service.producer.OrderProducer;
import com.example.Order_Service.repository.OrderRepository;
import com.example.common.constants.KafkaTopics;
import com.example.common.dto.OrderEvent;
import com.example.common.enums.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


/** Responsibilities:

 Convert request -> event
 Generate metadata
 Call producer
 */

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderProducer producer;

    @Autowired
    OrderRepository orderRepo;

    public void publishOrder(CreateOrderRequest orderRequest) {

        // saving ot mongo db
        OrderDocument orderDocument = new OrderDocument();
        orderDocument.setOrderId(orderRequest.getOrderId());
        orderDocument.setProduct(orderRequest.getProduct());
        orderDocument.setAmount(orderRequest.getAmount());
        orderDocument.setQuantity(orderRequest.getQuantity());
        orderDocument.setStatus(EventType.ORDER_CREATED);

        orderRepo.save(orderDocument);

        log.info("Saved order to MongoDB: {}",orderDocument.getOrderId());


        // setting fields to main class from dto
        OrderEvent orderEvent = new OrderEvent();

        //set manually
        orderEvent.setEventId(UUID.randomUUID().toString());
        orderEvent.setEventType(EventType.ORDER_CREATED);

        // set from incoming orderRequest
        orderEvent.setOrderId(orderRequest.getOrderId());
        orderEvent.setProduct(orderRequest.getProduct());
        orderEvent.setQuantity(orderRequest.getQuantity());
        orderEvent.setAmount(orderRequest.getAmount());

        orderEvent.setSource(KafkaTopics.ORDER_EVENTS);

        producer.send(orderEvent);

    }
}