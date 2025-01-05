package com.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
public class App {
    public static void main(String[] args) throws InterruptedException {
        example8_6();
    }

    private static void example8_1() {
        Flux.range(1, 5)
                .doOnRequest(data -> log.info("# doOnNext : {}", data))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @SneakyThrows
                    @Override
                    protected void hookOnNext(Integer value) {
                        Thread.sleep(2000);
                        log.info("# onNext: {}", value);
                        request(1);
                    }
                });
    }

    /**
     * 1. 0부터 1씩 증가를 0.001초에 한번씩 빠르게 emit
     * 2. Subscriber가 데이터 처리하는데 0.005초 시간이 걸리도록 함
     *    (Publisher에서 데이터를 emit하는 속도와 Subscriber가 전달받은 데이터를 처리하는 속도에 차이가 남 -> Backpressure 테스트 가능)
     */
    private static void example8_2() throws InterruptedException {
        Flux.interval(Duration.ofMillis(1L))
                .onBackpressureError()                                                      // ERROR 전략 적용
                .doOnNext(data -> log.info("# doOnNext : {}", data))
                .publishOn(Schedulers.parallel())                                           // Sequence 중 일부를 별도의 스레드에서 실행
                .subscribe(data -> {
                            try {
                                Thread.sleep(5L);
                            } catch (InterruptedException e) {}
                            log.info("# onNext: {}", data);
                        },
                        error -> log.error("# onError")
                );

        Thread.sleep(2000L);
    }

    private static void example8_3() throws InterruptedException {
        Flux.interval(Duration.ofMillis(1L))
                .onBackpressureDrop(dropped -> log.info("# dropped : {}", dropped))    // DROP 전략 적용
                .doOnNext(data -> log.info("# doOnNext : {}", data))
                .publishOn(Schedulers.parallel())                                           // Sequence 중 일부를 별도의 스레드에서 실행
                .subscribe(data -> {
                            try {
                                Thread.sleep(5L);
                            } catch (InterruptedException e) {}
                            log.info("# onNext: {}", data);
                        },
                        error -> log.error("# onError")
                );

        Thread.sleep(2000L);
    }

    private static void example8_4() throws InterruptedException {
        Flux.interval(Duration.ofMillis(1L))
                .onBackpressureLatest()
                .doOnNext(data -> log.info("# doOnNext : {}", data))
                .publishOn(Schedulers.parallel())                                           // Sequence 중 일부를 별도의 스레드에서 실행
                .subscribe(data -> {
                            try {
                                Thread.sleep(5L);
                            } catch (InterruptedException e) {}
                            log.info("# onNext: {}", data);
                        },
                        error -> log.error("# onError")
                );

        Thread.sleep(2000L);
    }

    private static void example8_5() throws InterruptedException {
        Flux.interval(Duration.ofMillis(1L))
                .doOnNext(data -> log.info("# emitted by original Flux : {}", data))
                .onBackpressureBuffer(2,
                        dropped -> log.info("** Overflow & Dropped : {} **", dropped),
                        BufferOverflowStrategy.DROP_LATEST
                )
                .doOnNext(data -> log.info("[ # emitted by Buffer :  {} ]", data))
                .publishOn(Schedulers.parallel(), false, 1)                                           // Sequence 중 일부를 별도의 스레드에서 실행
                .subscribe(data -> {
                            try {
                                Thread.sleep(5L);
                            } catch (InterruptedException e) {}
                            log.info("# onNext: {}", data);
                        },
                        error -> log.error("# onError")
                );

        Thread.sleep(3000L);
    }

    private static void example8_6() throws InterruptedException {
        Flux.interval(Duration.ofMillis(1L))
                .doOnNext(data -> log.info("# emitted by original Flux : {}", data))
                .onBackpressureBuffer(2,
                        dropped -> log.info("** Overflow & Dropped : {} **", dropped),
                        BufferOverflowStrategy.DROP_OLDEST
                )
                .doOnNext(data -> log.info("[ # emitted by Buffer :  {} ]", data))
                .publishOn(Schedulers.parallel(), false, 1)                                           // Sequence 중 일부를 별도의 스레드에서 실행
                .subscribe(data -> {
                            try {
                                Thread.sleep(5L);
                            } catch (InterruptedException e) {}
                            log.info("# onNext: {}", data);
                        },
                        error -> log.error("# onError")
                );

        Thread.sleep(3000L);
    }
}
