package ru.x5.demo.kafka.saga.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.x5.demo.kafka.saga.config.TaxiProperties;
import ru.x5.demo.kafka.saga.domain.TaxiOrder;
import ru.x5.demo.kafka.saga.dto.OrderUpdateDto;
import ru.x5.demo.kafka.saga.dto.ResultDto;
import ru.x5.demo.kafka.saga.enums.TaxiStatus;

@Service
public class KafkaOrderListener {

    private final Logger log = LoggerFactory.getLogger(KafkaOrderListener.class);

    private final TaxiService taxiService;
    private final KafkaTemplate<String, ResultDto> kafkaTemplate;

    private final TaxiProperties taxiProperties;

    public KafkaOrderListener(
            TaxiService taxiService,
            KafkaTemplate<String, ResultDto> kafkaTemplate,
            TaxiProperties taxiProperties,
            ObjectMapper mapper) {
        this.taxiService = taxiService;
        this.kafkaTemplate = kafkaTemplate;
        this.taxiProperties = taxiProperties;
    }

    @KafkaListener(topics = "${app.topic.income-order-topic}", groupId = "transfer")
    public void processIncome(@Payload OrderUpdateDto order, Acknowledgment acknowledgment) {

        log.info("Получен новый статус [{}] для заказа {}", order.getStatus(), order.getOrderId());

        if ("pending".equalsIgnoreCase(order.getStatus())) {
            newOrder(order.getOrderId(), acknowledgment);
            return;
        }
        if ("error".equalsIgnoreCase(order.getStatus())) {
            decline(order.getOrderId(), acknowledgment);
            return;
        }
        if ("done".equalsIgnoreCase(order.getStatus())) {
            approve(order.getOrderId(), acknowledgment);
        }
    }

    private void newOrder(Integer orderId, Acknowledgment acknowledgment) {
        log.info("Получен запрос на такси для заказа {}", orderId);
        ResultDto result = new ResultDto();
        result.setAuthor("transfer");
        try {

            // lets get some taxi
            TaxiOrder transfer = taxiService.getNewTaxi(orderId);
            result.setOrderId(orderId);
            result.setExtId(transfer.getId());
            result.setStatus(transfer.getStatus().name());

            kafkaTemplate.send(
                    taxiProperties.getOutcomeResultTopic(), result);
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("Отмечаем заказ такси как неудачный. Order = {}", orderId);
            result.setOrderId(orderId);
            result.setStatus(TaxiStatus.ERROR.name());
            kafkaTemplate.send(
                    taxiProperties.getOutcomeResultTopic(), result);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    private void decline(Integer orderId, Acknowledgment acknowledgment) {
        log.info("Отменяем заказ {}", orderId);
        taxiService.declineTaxi(orderId);
        acknowledgment.acknowledge();
    }

    private void approve(Integer orderId, Acknowledgment acknowledgment) {
        log.info("Одобряем заказ {}", orderId);
        taxiService.approveTaxi(orderId);
        acknowledgment.acknowledge();
    }
}
