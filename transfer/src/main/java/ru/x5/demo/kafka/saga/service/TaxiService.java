package ru.x5.demo.kafka.saga.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.x5.demo.kafka.saga.domain.TaxiOrder;
import ru.x5.demo.kafka.saga.enums.TaxiStatus;
import ru.x5.demo.kafka.saga.exceptions.TaxiNotFoundException;
import ru.x5.demo.kafka.saga.repository.TaxiOrderRepository;

import java.security.SecureRandom;

@Service
public class TaxiService {

    private final Logger log = LoggerFactory.getLogger(TaxiService.class);

    private final TaxiOrderRepository taxiOrderRepository;

    public TaxiService(TaxiOrderRepository taxiOrderRepository) {
        this.taxiOrderRepository = taxiOrderRepository;
    }

    @Transactional
    public TaxiOrder getNewTaxi(Integer orderId) {
        // some synthetic errors
        int random = new SecureRandom().nextInt(20);
        if (random == 0) {
            throw new TaxiNotFoundException("Не удалось заказать такси");
        }

        TaxiOrder taxiOrder = new TaxiOrder();
        taxiOrder.setOrderId(orderId);
        taxiOrder = taxiOrderRepository.save(taxiOrder);
        log.info("Заказан трансфер {} для заказа {}.", taxiOrder.getId(), orderId);
        return taxiOrder;
    }

    public void declineTaxi(Integer orderId) {
        TaxiOrder taxi = taxiOrderRepository.findByOrderId(orderId);
        if (taxi == null) {
            log.info("Для заказа {} нет трансфера.", orderId);
            return;
        }
        taxi.setStatus(TaxiStatus.ERROR);
        taxiOrderRepository.save(taxi);
    }

    public void approveTaxi(Integer orderId) {
        TaxiOrder taxi = taxiOrderRepository.findByOrderId(orderId);
        taxi.setStatus(TaxiStatus.APPROVED);
        taxiOrderRepository.save(taxi);
    }
}
