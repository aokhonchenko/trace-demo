package ru.x5.demo.kafka.saga.repository.rest;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.x5.demo.kafka.saga.domain.TaxiOrder;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "taxi_order", path = "taxi")
public interface TaxiRestRepository extends PagingAndSortingRepository<TaxiOrder, UUID> {
}
