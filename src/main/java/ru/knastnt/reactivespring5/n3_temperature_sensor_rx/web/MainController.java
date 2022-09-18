package ru.knastnt.reactivespring5.n3_temperature_sensor_rx.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String root() {
        return "index";
    }
}
