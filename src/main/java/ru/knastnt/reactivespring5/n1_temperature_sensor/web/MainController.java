package ru.knastnt.reactivespring5.n1_temperature_sensor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String root() {
        return "index";
    }
}
