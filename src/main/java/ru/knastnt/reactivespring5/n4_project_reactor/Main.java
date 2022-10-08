package ru.knastnt.reactivespring5.n4_project_reactor;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Flow;

@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("------------------------- простая подписка ---------------------------");
        Flux.just("A","B","C")
                .subscribe(
                        data -> log.info("onNext: {}", data),
                        err -> {},
                        () -> log.info("onComplete")
                );

        System.out.println("------------------------- подписка с обратным давлением ---------------------------");

        Flux.range(1, 100)
                .subscribe(
                        data -> log.info("onNext: {}", data),
                        err -> {},
                        () -> log.info("onComplete"),
                        subscription -> {
                            subscription.request(4);
                            subscription.cancel();
                        }
                );

        System.out.println("------------------------ бесконечный генераор с интервалом ----------------------------");

        Disposable disposable = Flux.interval(Duration.ofMillis(50))
                .subscribe(
                        data -> log.info("onNext: {}", data)
                );
        Thread.sleep(210L);
        disposable.dispose();

        System.out.println("------------------------ кастомный subscriber ----------------------------");

        CoreSubscriber<String> coreSubscriber = new CoreSubscriber<>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;
                log.info("onSubscribe. Initial request for 1 element.");
                subscription.request(1L);
            }

            @Override
            public void onNext(String item) {
                log.info("onNext: {}", item);
                log.info("Initial request for 1 element.");
                subscription.request(1L);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("onError", throwable);
            }

            @Override
            public void onComplete() {
                log.info("noComplete");
            }
        };
        Flux.just("Hello", "world", "!")
                .subscribe(coreSubscriber);
    }
}
