package ru.x5.demo.kafka.saga.ordersaga.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.x5.demo.kafka.saga.ordersaga.service.OrderService;

@RestController
@RequestMapping("/v1/order")
public class OrderClientController {

    private final OrderService orderService;

    public OrderClientController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create random order
     */
    @PostMapping
    public void createOrder() {
        orderService.createOrder();
    }

}
