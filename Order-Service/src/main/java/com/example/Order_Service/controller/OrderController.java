package com.example.Order_Service.controller;

import com.example.Order_Service.document.OrderDocument;
import com.example.Order_Service.dto.CreateOrderRequest;
import com.example.Order_Service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest orderRequest){

        System.out.println(orderRequest.getOrderId());
        System.out.println(orderRequest.getProduct());
        System.out.println(orderRequest.getQuantity());

        orderService.publishOrder(orderRequest);

        return  ResponseEntity.ok("Order event published successfully");
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderDocument> getOrderStatus(@PathVariable String orderId) {

        return orderService.getOrderStatus(orderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}