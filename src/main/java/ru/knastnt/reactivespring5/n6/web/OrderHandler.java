package ru.knastnt.reactivespring5.n6.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class OrderHandler {
    @Autowired
    private OrderRepository orderRepository;

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
//        return ServerResponse.ok().body(
//            orderRepository.get(serverRequest.pathVariable("id")),
//            Order.class
//        );
        return Mono.just(serverRequest)
                .map(s -> s.pathVariable("id"))
                .map(i -> orderRepository.get(i))
                .flatMap(o -> ServerResponse.ok().body(
                        o,
                        Order.class
                ));
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(Order.class)
                .flatMap(o -> orderRepository.save(o))
                .flatMap(
                        o -> ServerResponse
                                .created(URI.create("/orders/" + o.getId()))
                                .build()
                );
    }
}
