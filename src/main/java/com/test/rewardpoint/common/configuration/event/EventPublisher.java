package com.test.rewardpoint.common.configuration.event;

import org.springframework.context.ApplicationEventPublisher;

public class EventPublisher {

    private static ApplicationEventPublisher applicationEventPublisher;

    public static void raise(Object event) {
        applicationEventPublisher.publishEvent(event);
    }

    public static void setApplicationContext(ApplicationEventPublisher applicationEventPublisher) {
        EventPublisher.applicationEventPublisher = applicationEventPublisher;
    }
}
