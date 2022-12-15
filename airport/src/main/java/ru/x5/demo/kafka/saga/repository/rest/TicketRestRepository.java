package ru.x5.demo.kafka.saga.repository.rest;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.x5.demo.kafka.saga.domain.AirTicket;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "air_ticket", path = "tickets")
public interface TicketRestRepository extends PagingAndSortingRepository<AirTicket, UUID> {
}
