package ru.x5.demo.kafka.saga.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.x5.demo.kafka.saga.domain.HotelRoom;
import ru.x5.demo.kafka.saga.enums.RoomStatus;
import ru.x5.demo.kafka.saga.exceptions.RoomNotFoundException;
import ru.x5.demo.kafka.saga.repository.HotelRoomRepository;

import java.security.SecureRandom;

@Service
public class HotelRoomService {

    private final Logger log = LoggerFactory.getLogger(HotelRoomService.class);

    private final HotelRoomRepository hotelRoomRepository;

    public HotelRoomService(HotelRoomRepository hotelRoomRepository) {
        this.hotelRoomRepository = hotelRoomRepository;
    }

    @Transactional
    public HotelRoom getNewRoom(Integer orderId) {
        // some synthetic errors
        int random = new SecureRandom().nextInt(20);
        if (random == 0) {
            throw new RoomNotFoundException("Не удалось заказать комнату");
        }

        HotelRoom hotelRoom = new HotelRoom();
        hotelRoom.setOrderId(orderId);
        hotelRoom = hotelRoomRepository.save(hotelRoom);
        log.info("Заказана комната {} для заказа {}.", hotelRoom.getId(), orderId);
        return hotelRoom;
    }

    public void declineRoom(Integer orderId) {
        HotelRoom room = hotelRoomRepository.findByOrderId(orderId);
        if (room == null) {
            log.info("Для заказа {} нет комнаты.", orderId);
            return;
        }
        room.setStatus(RoomStatus.ERROR);
        hotelRoomRepository.save(room);
    }

    public void approveRoom(Integer orderId) {
        HotelRoom room = hotelRoomRepository.findByOrderId(orderId);
        room.setStatus(RoomStatus.APPROVED);
        hotelRoomRepository.save(room);
    }
}
