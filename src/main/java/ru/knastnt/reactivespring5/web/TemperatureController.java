package ru.knastnt.reactivespring5.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.knastnt.reactivespring5.temper.Temperature;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@RestController
public class TemperatureController {
    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @GetMapping("/temperature-stream")
    public SseEmitter temperatureStream(HttpServletRequest request){
        SseEmitter emitter = new SseEmitter();
        clients.add(emitter);

        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onCompletion(() -> clients.remove(emitter));

        return emitter;
    }

    @Async
    @EventListener
    public void handleTemperature(Temperature temperature){
        log.info("handled temperature {}", temperature.getTemperature());
        Set<SseEmitter> deadEmitters = new HashSet<>();
        clients.forEach(sseEmitter -> {
            try{
                sseEmitter.send(temperature, MediaType.APPLICATION_JSON);
            } catch (Exception e){
                deadEmitters.add(sseEmitter);
            }
        });
        clients.removeAll(deadEmitters);
    }
}
