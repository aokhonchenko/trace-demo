package ru.x5.demo.kafka.saga.dto;

public class OrderUpdateDto {

    private Integer orderId;
    private String status;

    // region g/s

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // endregion

}
