package ru.x5.demo.kafka.saga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.x5.demo.kafka.saga.domain.TaxiOrder;

@Repository
public interface TaxiOrderRepository extends JpaRepository<TaxiOrder, Integer> {

    TaxiOrder findByOrderId(Integer orderId);
}
