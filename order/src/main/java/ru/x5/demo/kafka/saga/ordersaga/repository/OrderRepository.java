package ru.x5.demo.kafka.saga.ordersaga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.x5.demo.kafka.saga.ordersaga.domain.Order;
import ru.x5.demo.kafka.saga.ordersaga.enums.OrderStatus;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findOrderByIdAndOrderStatus(Integer id, OrderStatus status);

}
