package com.example.Order_Service.service;

import com.example.Order_Service.document.OrderDocument;
import com.example.Order_Service.dto.CreateOrderRequest;
import com.example.Order_Service.exception.DuplicateOrderException;
import com.example.Order_Service.producer.OrderProducer;
import com.example.Order_Service.repository.OrderRepository;
import com.example.common.constants.KafkaTopics;
import com.example.common.dto.OrderEvent;
import com.example.common.enums.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

        Optional<OrderDocument> existingOrder = orderRepo.findByOrderId(orderRequest.getOrderId());

        if (existingOrder.isPresent()){

            log.warn("[ORDER] Duplicate order detected with Order-ID: {} ",orderRequest.getOrderId());

            throw new DuplicateOrderException(
                    "Order already exist with ID: "
                    + orderRequest.getOrderId()
            );
        }

        // saving ot mongo db
        OrderDocument orderDocument = new OrderDocument();

        orderDocument.setOrderId(orderRequest.getOrderId());
        orderDocument.setProduct(orderRequest.getProduct());
        orderDocument.setQuantity(orderRequest.getQuantity());
        orderDocument.setStatus("PROCESSING");

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

        orderEvent.setSource(KafkaTopics.ORDER_EVENTS);

        producer.send(orderEvent);

    }


    public Optional<OrderDocument> getOrderStatus(String orderId){

        return orderRepo.findByOrderId(orderId);

    }
}