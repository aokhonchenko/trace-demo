package ru.x5.demo.kafka.saga.repository.rest;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.x5.demo.kafka.saga.domain.HotelRoom;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "hotel_room", path = "rooms")
public interface RoomRestRepository extends PagingAndSortingRepository<HotelRoom, UUID> {
}
