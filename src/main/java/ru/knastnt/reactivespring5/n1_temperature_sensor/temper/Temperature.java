package ru.knastnt.reactivespring5.n1_temperature_sensor.temper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Temperature {
    private final double temperature;
}
