package ru.x5.demo.kafka.saga.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.x5.demo.kafka.saga.config.AirportProperties;
import ru.x5.demo.kafka.saga.domain.AirTicket;
import ru.x5.demo.kafka.saga.dto.OrderUpdateDto;
import ru.x5.demo.kafka.saga.dto.ResultDto;
import ru.x5.demo.kafka.saga.enums.TicketStatus;

@Service
public class KafkaOrderListener {

    private final Logger log = LoggerFactory.getLogger(KafkaOrderListener.class);

    private final AirTicketService airTicketService;
    private final KafkaTemplate<String, ResultDto> kafkaTemplate;

    private final AirportProperties airportProperties;

    public KafkaOrderListener(
            AirTicketService airTicketService,
            KafkaTemplate<String, ResultDto> kafkaTemplate,
            AirportProperties airportProperties,
            ObjectMapper mapper) {
        this.airTicketService = airTicketService;
        this.kafkaTemplate = kafkaTemplate;
        this.airportProperties = airportProperties;
    }

    @KafkaListener(topics = "${app.topic.income-order-topic}", groupId = "airport")
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
        log.info("Получен запрос на авиабилет для заказа {}", orderId);
        ResultDto result = new ResultDto();
        result.setAuthor("airport");
        try {

            // let get some ticket
            final AirTicket ticket = airTicketService.getNewTicket(orderId);
            result.setOrderId(orderId);
            result.setExtId(ticket.getId());
            result.setStatus(ticket.getStatus().name());

            kafkaTemplate.send(
                    airportProperties.getOutcomeResultTopic(), result);
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("Отмечаем заказ билета как неудачный. Order = {}", orderId);
            result.setOrderId(orderId);
            result.setStatus(TicketStatus.ERROR.name());
            result.setAuthor("airport");
            kafkaTemplate.send(
                    airportProperties.getOutcomeResultTopic(),
                    result);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    private void decline(Integer orderId, Acknowledgment acknowledgment) {
        log.info("Отменяем заказ {}", orderId);
        airTicketService.declineTicket(orderId);
        acknowledgment.acknowledge();
    }

    private void approve(Integer orderId, Acknowledgment acknowledgment) {
        log.info("Отменяем заказ {}", orderId);
        airTicketService.approveTicket(orderId);
        acknowledgment.acknowledge();
    }
}
