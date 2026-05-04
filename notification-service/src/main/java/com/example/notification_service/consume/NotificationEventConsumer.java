package com.example.notification_service.consume;

import com.example.common.constants.KafkaTopics;
import com.example.common.dto.OrderEvent;
import com.example.common.enums.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
//import tools.jackson.databind.ObjectMapper;

@Component
public class NotificationEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);
    private final ObjectMapper objectMapper;


    public NotificationEventConsumer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = KafkaTopics.ORDER_EVENTS,
            groupId = "notification-group"
    )
    public void consume(String message){

        try {
            OrderEvent orderEvent = objectMapper.readValue(message, OrderEvent.class);

            if (orderEvent.getEventType() == EventType.ORDER_CREATED){

                log.info("[NOTIFICATION] Notification received for {}", orderEvent.getOrderId());


            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
