package com.example.inventory_service.service;

import com.example.inventory_service.document.ProductStockDocument;
import com.example.inventory_service.dto.AddStockRequest;
import com.example.inventory_service.repository.ProductStockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Slf4j
@Service
public class StockService {

    @Autowired
    ProductStockRepository productStockRepo;

    // add Stock to ProductStock repo
    public String addStock(AddStockRequest addStockRequest){

        try{
            Optional<ProductStockDocument> currentStockDocument = productStockRepo.findByProduct(addStockRequest.getProduct());

            ProductStockDocument newStockDocument = new ProductStockDocument();

            // if product already available
            if (currentStockDocument.isPresent()){

                newStockDocument = currentStockDocument.get();
                newStockDocument.setAvailableStock((currentStockDocument.get().getAvailableStock()) + (addStockRequest.getQuantity()));

                log.info("Product: {} already found. Adding to existing entry",currentStockDocument.get().getProduct());

            }   else {
                // if product not already available

                newStockDocument.setProduct(addStockRequest.getProduct());
                newStockDocument.setAvailableStock(addStockRequest.getQuantity());
                newStockDocument.setUnitPrice(addStockRequest.getUnitPrice());

                log.info("Adding {} as new entry with unit price: {} ", addStockRequest.getProduct() ,addStockRequest.getUnitPrice());
            }

            productStockRepo.save(newStockDocument);

        } catch (Exception e){
            log.warn("[INVENTORY] Exception while adding stock"+e);

            return "Failed to add stock";
        }

        return "Stock Added : "
                + addStockRequest.getProduct()
                + " -> "
                + addStockRequest.getQuantity()
                + " -> "
                + addStockRequest.getUnitPrice();

    }

}
