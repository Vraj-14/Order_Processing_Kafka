package com.example.common.dto;

import com.example.common.enums.EventType;
import com.example.common.enums.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResultEvent {

    private String orderId;
    private String product;
    private int quantity;
    private BigDecimal unitPrice;
    private InventoryStatus status;
    private String reason;
}
