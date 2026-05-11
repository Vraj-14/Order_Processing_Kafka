package com.example.inventory_service.controller;

import com.example.inventory_service.document.ProductStockDocument;
import com.example.inventory_service.dto.AddStockRequest;
import com.example.inventory_service.repository.ProductStockRepository;
import com.example.inventory_service.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin/stock")
public class StockController {

    @Autowired
    ProductStockRepository productStockRepo;

    @Autowired
    StockService stockService;

    // admin side
    // adds product and quantity to Stock Repo

    @PostMapping("/add")
    public ResponseEntity<String> addStock(
            @RequestBody AddStockRequest addStockRequest){

        String response = stockService.addStock(addStockRequest);

        return ResponseEntity.ok(response);
    }
}
