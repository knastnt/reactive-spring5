package ru.knastnt.reactivespring5.n2_rx_java;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class RxJavaMain1 {
    private static ObservableOnSubscribe<String> stringObservableOnSubscribe = emitter -> {
        emitter.onNext("Hello!!!");
        emitter.onNext("Hello!!!");
        emitter.onNext("Hello!!!");
        emitter.onNext("Hello!!!");
        emitter.onNext("Hello!!!");
        emitter.onComplete();
    };

    private static Observable<String> observable = Observable.create(stringObservableOnSubscribe);

    private static Observer<String> subscriber = new Observer<String>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            System.out.println("Subscriber: Subscribed: " + d.toString());
        }

        @Override
        public void onNext(@NonNull String s) {
            System.out.println("Subscriber: " + s);
        }

        @Override
        public void onError(@NonNull Throwable throwable) {
            System.err.println("Subscriber: " + throwable);
        }

        @Override
        public void onComplete() {
            System.out.println("Subscriber: Done!");
        }
    };

    public static void main(String[] args) throws InterruptedException {
        observable.subscribe(subscriber);
    }
}
