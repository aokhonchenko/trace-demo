package ru.x5.demo.kafka.saga.ordersaga.repository.rest;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.x5.demo.kafka.saga.ordersaga.domain.Order;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "orders", path = "orders")
public interface OrderRestRepository extends PagingAndSortingRepository<Order, UUID> {
}
