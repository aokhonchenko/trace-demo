package ru.x5.demo.kafka.saga.ordersaga.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.x5.demo.kafka.saga.dto.OrderUpdateDto;
import ru.x5.demo.kafka.saga.ordersaga.config.AppProperties;
import ru.x5.demo.kafka.saga.ordersaga.domain.Order;
import ru.x5.demo.kafka.saga.ordersaga.enums.OrderStatus;
import ru.x5.demo.kafka.saga.ordersaga.repository.OrderRepository;

import java.util.function.Function;

@Service
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderUpdateDto> kafkaTemplate;

    private final AppProperties appProperties;
    private Function<Order, OrderUpdateDto> toOrderUpdate = order -> {
        final OrderUpdateDto orderUpdate = new OrderUpdateDto();
        orderUpdate.setOrderId(order.getId());
        orderUpdate.setStatus(order.getOrderStatus().name());
        return orderUpdate;
    };

    public OrderService(
            OrderRepository orderRepository,
            KafkaTemplate<String, OrderUpdateDto> kafkaTemplate,
            AppProperties appProperties) {
        this.orderRepository = orderRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.appProperties = appProperties;
    }

    @Transactional
    public void createOrder() {
        Order order = new Order();
        order = orderRepository.save(order);
        log.info("Сформирован заказ под номером {}", order.getId());
        sendToKafka(order);
    }

    public void sendToKafka(Order order) {
        kafkaTemplate.send(appProperties.getOrderTopic(), toOrderUpdate.apply(order));
    }

    @Transactional
    public void declineOrder(Integer orderId) {
        log.info("Отменяем заказ {}", orderId);
        Order order = orderRepository.findOrderByIdAndOrderStatus(orderId, OrderStatus.PENDING).orElse(null);
        if (order == null) {
            log.info("Заказ {} не найден или не в статусе ожидания - ничего не требуется", orderId);
            return;
        }
        order.setOrderStatus(OrderStatus.ERROR);
        sendToKafka(order);
        orderRepository.save(order);
        log.info("Заказ {} отменен", orderId);
    }

    @Transactional
    public void updateOrder(Integer orderId, String author, Integer extId) {
        log.info("Обновляем заказ {}", orderId);
        Order order = orderRepository.findOrderByIdAndOrderStatus(orderId, OrderStatus.PENDING).orElse(null);
        if (order == null) {
            log.info("Заказ {} не найден или не в статусе ожидания - ничего не требуется", orderId);
            return;
        }
        switch (author) {
            case "airport" -> order.setAirTicket(extId);
            case "transfer" -> order.setTransfer(extId);
            case "hotel" -> order.setRoom(extId);
            default -> log.info("Не определен источник обновления[{}] для заказа {}", author, orderId);
        }
        if (order.isCompleted()) {
            order.setOrderStatus(OrderStatus.DONE);
            sendToKafka(order);
            orderRepository.save(order);
            sendNotify(order);
        }
    }

    private void sendNotify(Order order) {
        log.info("Нотифицируем сторонние системы об успехе заказа {}", order.getId());
        kafkaTemplate.send(appProperties.getNotifyTopic(), toOrderUpdate.apply(order));
    }
}
