package ru.knastnt.reactivespring5.n6.web;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderRepository {
    private Map<Long, Order> db = new ConcurrentHashMap<>();

    public Mono<Order> save(Order order) {
        return Mono.defer(() -> {
            System.out.println("Repository, save!!!");
            db.put(order.getId(), order);
            return Mono.just(order);
        });
    }

    public Mono<Order> get(String id) {
        return Mono.defer(() -> {
            System.out.println("Repository, get!!!");
            Order data = db.get(Long.parseLong(id));
            return data != null ? Mono.just(data) : Mono.empty();
        });
    }
}
