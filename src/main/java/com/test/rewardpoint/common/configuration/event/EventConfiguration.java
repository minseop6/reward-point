package com.test.rewardpoint.common.configuration.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EventConfiguration {

    private final ApplicationContext applicationContext;

    @PostConstruct
    public void setPublisher() {
        EventPublisher.setApplicationContext(applicationContext);
    }
}
