package ru.x5.demo.kafka.saga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.x5.demo.kafka.saga.domain.AirTicket;

@Repository
public interface AirportTicketRepository extends JpaRepository<AirTicket, Integer> {

    AirTicket findByOrderId(Integer orderId);
}
