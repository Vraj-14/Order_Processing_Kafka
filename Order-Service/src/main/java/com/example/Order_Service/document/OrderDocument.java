package com.example.Order_Service.document;

import com.example.common.enums.EventType;
import jdk.jfr.DataAmount;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Document(collection = "orders")
public class OrderDocument {

    @Id
    private String id;          // MongoDB auto-generates this (_id)

    private String orderId;
    private String product;
    private int quantity;
    private BigDecimal amount;
    private EventType status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public EventType getStatus() {
        return status;
    }

    public void setStatus(EventType status) {
        this.status = status;
    }
}
