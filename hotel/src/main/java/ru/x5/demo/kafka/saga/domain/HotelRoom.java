package ru.x5.demo.kafka.saga.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import ru.x5.demo.kafka.saga.enums.RoomStatus;

import java.time.LocalDateTime;

@Entity
public class HotelRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer orderId;

    private RoomStatus status = RoomStatus.PENDING;
    @CreatedDate
    private LocalDateTime dateTime;

    // region getters / setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    // endregion

}
