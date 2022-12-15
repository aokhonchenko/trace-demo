package ru.x5.demo.kafka.saga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.x5.demo.kafka.saga.domain.HotelRoom;

@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Integer> {

    HotelRoom findByOrderId(Integer orderId);
}
