package ru.x5.demo.kafka.saga.ordersaga.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.kafka")
public class AppProperties {

    private String orderTopic;
    private String resultTopic;

    private String notifyTopic;

    //region getters / setters

    public String getOrderTopic() {
        return orderTopic;
    }

    public void setOrderTopic(String orderTopic) {
        this.orderTopic = orderTopic;
    }

    public String getResultTopic() {
        return resultTopic;
    }

    public void setResultTopic(String resultTopic) {
        this.resultTopic = resultTopic;
    }

    public String getNotifyTopic() {
        return notifyTopic;
    }

    public void setNotifyTopic(String notifyTopic) {
        this.notifyTopic = notifyTopic;
    }

    //endregion


}
