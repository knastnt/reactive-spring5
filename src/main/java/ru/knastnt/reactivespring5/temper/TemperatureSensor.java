package ru.knastnt.reactivespring5.temper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class TemperatureSensor {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private Random rnd = new Random();
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    void init() {
        executorService.schedule(this::probe, 1, TimeUnit.SECONDS);
    }

    private void probe() {
        double temp = 16 + rnd.nextGaussian() * 10;
        eventPublisher.publishEvent(new Temperature(temp));

        executorService.schedule(this::probe, rnd.nextInt(5000), TimeUnit.MILLISECONDS);
    }
}
