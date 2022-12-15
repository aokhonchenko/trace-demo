package ru.x5.demo.kafka.saga.ordersaga.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.x5.demo.kafka.saga.dto.ResultDto;

import java.time.Duration;

@Service
public class KafkaResultListener {

    private final Logger log = LoggerFactory.getLogger(KafkaResultListener.class);

    private final OrderService orderService;

    public KafkaResultListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${app.kafka.result-topic}", groupId = "order", containerFactory = "kafkaListenerContainerFactory")
    public void resultIncome(@Payload ResultDto result, Acknowledgment acknowledgment) {
        log.info("Получено обновление от {} для заказа {}. Полученный статус - {}",
                result.getAuthor(),
                result.getOrderId(),
                result.getStatus());
        try {
            Integer orderId = result.getOrderId();

            if ("error".equalsIgnoreCase(result.getStatus())) {
                orderService.declineOrder(orderId);
            } else {
                orderService.updateOrder(result.getOrderId(), result.getAuthor(), result.getExtId());
            }
        } catch (Exception e) {
            acknowledgment.nack(Duration.ofSeconds(15));
        }
    }

}
