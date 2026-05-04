package com.example.common.dto;

import com.example.common.enums.EventType;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;


import java.math.BigDecimal;
import java.time.Instant;

/** belongs to messaging layer
 represents Kafka message contract */

public class OrderEvent {

    // metadata
    private String eventId;
    private EventType eventType;


    // business data
    private String orderId;
    private String product;
    private BigDecimal amount;
    private int quantity;

    private String source;

    public OrderEvent(){
    }

    public OrderEvent(
            String eventId,
            EventType eventType,
            String orderId,
            String product,
            BigDecimal amount,
            int quantity,
            String source
    ){
        this.eventId = eventId;
        this.eventType = eventType;
        this.orderId = orderId;
        this.product = product;
        this.amount = amount;
        this.quantity=quantity;
        this.source = source;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}