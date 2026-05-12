package com.example.common.dto;

import com.example.common.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResultEvent {

    private String orderId;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;

}
