package ru.knastnt.reactivespring5.n2_rx_java;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

//see https://rxmarbles.com

@Slf4j
public class RxJavaMain2 {
    public static void main(String[] args) throws InterruptedException {
        Observable.<String>create(emitter -> {
            emitter.onNext("Hello!!!");
            if (Math.random() > 0.5) throw new RuntimeException("AAAAAAAAAAAA!");
            emitter.onComplete();
        })
        .subscribe(
                (s) -> log.info("Subscriber: " + s),
                (throwable) -> log.error("Subscriber: " + throwable),
                () -> log.info("Subscriber: Done!")
        );

        ///////////////////////////////////////////////////////////////////////////////////////////

        Observable<String> observable1 = Observable.just(1, 2, 3, 4, 5)
                .map(integer -> "Hello " + integer + "!!!");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Observable<String> observable2 = Observable.fromFuture(
                executorService.submit(() -> {
                    Thread.sleep(1500);
                    return "Hi!";
                })
        );

        Observable.concat(observable1, observable2)
                .subscribe(log::info);
        executorService.shutdown();

        ///////////////////////////////////////////////////////////////////////////////////////////

        CountDownLatch countDownLatch = new CountDownLatch(3);

        Observable.interval(500, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    log.info("countDown");
                    countDownLatch.countDown();
                });

        Disposable disposable = Observable.interval(200, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> log.info("Received: " + aLong));
        countDownLatch.await();

        disposable.dispose();

        ///////////////////////////////////////////////////////////////////////////////////////////

        Observable.zip(
                Observable.just("a", "b", "c", "d"),
                Observable.just(1, 2, 3, 4),
                (s, integer) -> s + integer
        )
        .subscribe(log::info);

        Observable.zip(
                Observable.interval(20, TimeUnit.MILLISECONDS).take(10),
                Observable.interval(7, TimeUnit.MILLISECONDS).take(8),
                (s1, s2) -> s1 + "-" + s2
        )
        .subscribe(log::info);
        Thread.sleep(1000L);

        //see https://rxmarbles.com
    }
}
