package ru.knastnt.reactivespring5.n3_temperature_sensor_rx.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.knastnt.reactivespring5.n3_temperature_sensor_rx.temper.Temperature;
import ru.knastnt.reactivespring5.n3_temperature_sensor_rx.temper.TemperatureSensor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@RestController
public class TemperatureController {
    @Autowired
    private TemperatureSensor temperatureSensor;

    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @GetMapping("/ts")
    public SseEmitter temperatureStream(HttpServletRequest request){
        SseEmitter emitter = new SseEmitter();
        log.debug("add emitter {}", emitter);
        clients.add(emitter);

        emitter.onTimeout(() -> this.removeEmitter(emitter));
        emitter.onCompletion(() -> this.removeEmitter(emitter));

        return emitter;
    }

    @PostConstruct
    private void init() {
        temperatureSensor
                .temperatureStream()
                .subscribe(this::handleTemperature);
        System.out.println("End of init");
    }

    public void handleTemperature(Temperature temperature){
        log.info("handled temperature {}", temperature.getTemperature());
        Set<SseEmitter> deadEmitters = new HashSet<>();
        log.debug("send to {} client(s)", clients.size());
        clients.forEach(sseEmitter -> {
            try{
                sseEmitter.send(temperature, MediaType.APPLICATION_JSON);
            } catch (Exception e){
                deadEmitters.add(sseEmitter);
            }
        });
        deadEmitters.forEach(this::removeEmitter);
    }

    private void removeEmitter(SseEmitter emitter){
        log.debug("remove emitter {}", emitter);
        clients.remove(emitter);
    }
}
