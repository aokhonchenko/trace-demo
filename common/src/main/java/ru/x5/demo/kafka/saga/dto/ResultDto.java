package ru.x5.demo.kafka.saga.dto;

public class ResultDto {

    private String author;
    private Integer extId;
    private String status;
    private Integer orderId;

    //region g/s

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getExtId() {
        return extId;
    }

    public void setExtId(Integer extId) {
        this.extId = extId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    //endregion

}