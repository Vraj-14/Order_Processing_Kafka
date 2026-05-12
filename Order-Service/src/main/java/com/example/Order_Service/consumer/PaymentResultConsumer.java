package com.example.Order_Service.consumer;


import com.example.Order_Service.repository.OrderRepository;
import com.example.common.constants.KafkaTopics;
import com.example.common.dto.PaymentResultEvent;
import com.example.common.enums.PaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.event.ExceptionEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentResultConsumer {

    private final ObjectMapper objectMapper;
    private OrderRepository orderRepository;

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_RESULT,
            groupId = "order-group"
    )
    public void consume(String message){

        try{
            // comes from order-service
            PaymentResultEvent paymentResultEvent = objectMapper.readValue(message,PaymentResultEvent.class);

            // fetch the order based on incoming message's orderId from Order repo
            orderRepository.findByOrderId(paymentResultEvent.getOrderId()).ifPresent(order -> {

                // set totalAmount & status from payment service through kafka
                order.setTotalAmount(paymentResultEvent.getTotalAmount());
                order.setStatus(
                        paymentResultEvent.getPaymentStatus() == PaymentStatus.PAYMENT_SUCCESS
                                ? PaymentStatus.PAYMENT_SUCCESS.toString() : PaymentStatus.PAYMENT_FAILED.toString()
                );

                // save the same order to Order repo again; here, totalAmount & status are set again from payment service
                orderRepository.save(order);

                log.info("[ORDER] Updated order {} → status={} totalAmount={}",
                        paymentResultEvent.getOrderId(), order.getStatus(), order.getTotalAmount());
            });

        } catch (Exception e){
            log.warn("[ORDER] Error consuming payment result",e);
        }

    }






































}
