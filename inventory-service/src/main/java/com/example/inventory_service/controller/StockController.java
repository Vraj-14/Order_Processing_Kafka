package com.example.inventory_service.controller;

import com.example.inventory_service.document.ProductStockDocument;
import com.example.inventory_service.dto.AddStockRequest;
import com.example.inventory_service.repository.ProductStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/stock")
public class StockController {

    @Autowired
    ProductStockRepository productStockRepo;

    // admin side
    // adds product and quantity to Stock Repo

    @PostMapping("/add")
    public ResponseEntity<String> addStock(
            @RequestBody AddStockRequest addStockRequest){

        ProductStockDocument stockDocument = new ProductStockDocument();

        stockDocument.setProduct(addStockRequest.getProduct());
        stockDocument.setTotalStock(addStockRequest.getQuantity());
        stockDocument.setAvailableStock(addStockRequest.getQuantity());

        productStockRepo.save(stockDocument);

        return ResponseEntity.ok("Stock Added : "+addStockRequest.getProduct()+" -> "+addStockRequest.getQuantity());
    }

}
