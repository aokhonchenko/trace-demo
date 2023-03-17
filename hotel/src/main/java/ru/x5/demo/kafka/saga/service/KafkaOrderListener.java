package ru.x5.demo.kafka.saga.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.x5.demo.kafka.saga.config.HotelProperties;
import ru.x5.demo.kafka.saga.domain.HotelRoom;
import ru.x5.demo.kafka.saga.dto.OrderUpdateDto;
import ru.x5.demo.kafka.saga.dto.ResultDto;
import ru.x5.demo.kafka.saga.enums.RoomStatus;

@Service
public class KafkaOrderListener {

    private final Logger log = LoggerFactory.getLogger(KafkaOrderListener.class);

    private final HotelRoomService hotelRoomService;
    private final KafkaTemplate<String, ResultDto> kafkaTemplate;

    private final HotelProperties hotelProperties;

    private final ObjectMapper mapper;

    public KafkaOrderListener(
            HotelRoomService hotelRoomService,
            KafkaTemplate<String, ResultDto> kafkaTemplate,
            HotelProperties hotelProperties,
            ObjectMapper mapper) {
        this.hotelRoomService = hotelRoomService;
        this.kafkaTemplate = kafkaTemplate;
        this.hotelProperties = hotelProperties;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "${app.topic.income-order-topic}", groupId = "hotel")
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
        log.info("Получен запрос на комнату для заказа {}", orderId);
        ResultDto result = new ResultDto();
        result.setAuthor("hotel");
        try {

            // lets get some room
            HotelRoom roomId = hotelRoomService.getNewRoom(orderId);
            result.setOrderId(orderId);
            result.setExtId(roomId.getId());
            result.setStatus(roomId.getStatus().name());

            kafkaTemplate.send(
                    hotelProperties.getOutcomeResultTopic(), result);
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("Отмечаем заказ комнаты как неудачный. Order = {}", orderId);
            result.setAuthor("hotel");
            result.setOrderId(orderId);
            result.setStatus(RoomStatus.ERROR.name());
            kafkaTemplate.send(
                    hotelProperties.getOutcomeResultTopic(), result);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    private void decline(Integer orderId, Acknowledgment acknowledgment) {
        log.info("Отменяем заказ {}", orderId);
        hotelRoomService.declineRoom(orderId);
        acknowledgment.acknowledge();
    }

    private void approve(Integer orderId, Acknowledgment acknowledgment) {
        log.info("Одобряем заказ {}", orderId);
        hotelRoomService.approveRoom(orderId);
        acknowledgment.acknowledge();
    }
}
