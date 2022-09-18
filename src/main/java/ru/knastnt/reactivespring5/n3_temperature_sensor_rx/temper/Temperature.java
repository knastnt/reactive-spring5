package ru.knastnt.reactivespring5.n3_temperature_sensor_rx.temper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Temperature {
    private final double temperature;
}
