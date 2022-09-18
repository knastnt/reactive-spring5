package ru.knastnt.reactivespring5.n3_temperature_sensor_rx.temper;

import io.reactivex.rxjava3.core.Observable;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class TemperatureSensor {
    private Random rnd = new Random();

    public Observable<Temperature> temperatureStream() {
        return Observable
                .range(0, Integer.MAX_VALUE)
                .concatMap(tick -> Observable
                    .just(tick)
                    .delay(rnd.nextInt(5000), TimeUnit.MILLISECONDS)
                    .map(tickDelayed -> 16 + rnd.nextGaussian() * 10)
                    .map(Temperature::new)
                )
                .publish()
                .refCount();
    }
}
