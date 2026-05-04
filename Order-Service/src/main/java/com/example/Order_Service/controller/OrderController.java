package com.example.Order_Service.controller;

import com.example.Order_Service.dto.CreateOrderRequest;
import com.example.Order_Service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        System.out.println(orderRequest.getAmount());

        orderService.publishOrder(orderRequest);

        return  ResponseEntity.ok("Order event published successfully");
    }
}