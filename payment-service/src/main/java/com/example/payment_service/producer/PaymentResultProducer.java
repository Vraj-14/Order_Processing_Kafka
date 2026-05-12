package com.example.payment_service.producer;

// publishes payment result

import com.example.common.constants.KafkaTopics;
import com.example.common.dto.PaymentResultEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentResultProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(PaymentResultEvent paymentResultEvent){

        try{
            String json = objectMapper.writeValueAsString(paymentResultEvent);

            kafkaTemplate.send(
                    KafkaTopics.PAYMENT_RESULT,
                    paymentResultEvent.getOrderId(),
                    json)
                    .whenComplete((result, ex) -> {
                if (ex != null)
                    log.error("[PAYMENT] Failed to publish result", ex);
                else
                    log.info("[PAYMENT] Published {} for order: {}",
                        paymentResultEvent.getPaymentStatus(), paymentResultEvent.getOrderId());
            });


        } catch (Exception e){
            log.warn("[PAYMENT] Serialization error",e);
        }

    }


}
