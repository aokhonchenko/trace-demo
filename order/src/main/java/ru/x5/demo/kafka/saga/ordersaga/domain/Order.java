package ru.x5.demo.kafka.saga.ordersaga.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.x5.demo.kafka.saga.ordersaga.enums.OrderStatus;

import java.time.LocalDateTime;

@Table
@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime updated;

    private Integer airTicket;

    private Integer transfer;

    private Integer room;

    @Version
    private Integer version = 0;

    public boolean isCompleted() {
        return airTicket != null && transfer != null && room != null;
    }

    //region getters and setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Integer getAirTicket() {
        return airTicket;
    }

    public void setAirTicket(Integer airTicket) {
        this.airTicket = airTicket;
    }

    public Integer getTransfer() {
        return transfer;
    }

    public void setTransfer(Integer transfer) {
        this.transfer = transfer;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    //endregion

}
