package ru.x5.demo.kafka.saga.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.topic")
public class AirportProperties {

    private String incomeOrderTopic;
    private String outcomeResultTopic;

    // region getters and setters
    public String getIncomeOrderTopic() {
        return incomeOrderTopic;
    }

    public void setIncomeOrderTopic(String incomeOrderTopic) {
        this.incomeOrderTopic = incomeOrderTopic;
    }

    public String getOutcomeResultTopic() {
        return outcomeResultTopic;
    }

    public void setOutcomeResultTopic(String outcomeResultTopic) {
        this.outcomeResultTopic = outcomeResultTopic;
    }
    // endregion

}
