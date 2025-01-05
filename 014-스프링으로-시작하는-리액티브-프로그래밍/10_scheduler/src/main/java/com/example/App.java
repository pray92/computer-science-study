package com.example;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class App {
    public static void main(String[] args) throws InterruptedException {
        example10_8();
    }

    private static void example10_1() throws InterruptedException {
        Flux.fromArray(new Integer[]{1, 3, 5, 7})
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(data -> log.info("# doOnNext: {}", data))
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(500L);
    }

    private static void example10_2() throws InterruptedException {
        Flux.fromArray(new Integer[]{1, 3, 5, 7})
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(data -> log.info("# doOnNext: {}", data))
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe"))
                .publishOn(Schedulers.parallel())
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(500L);
    }

    private static void example10_3() throws InterruptedException {
        Flux.fromArray(new Integer[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19})
                .parallel()                                     // emit 데이터를 CPU의 논리적 코어(물리저리적 스레드) 수에 맞게 분배
                .runOn(Schedulers.parallel())                   // 실제로 병렬 작업을 수행할 스레드의 할당은 runOn이 담당
                .subscribe(data -> log.info("# onNext : {} ", data));

        Thread.sleep(100L);
    }

    private static void example10_4() throws InterruptedException {
        Flux.fromArray(new Integer[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19})
                .parallel(4)                                     // 스레드 개수 지정 가능
                .runOn(Schedulers.parallel())
                .subscribe(data -> log.info("# onNext : {} ", data));

        Thread.sleep(100L);
    }

    /**
     * 메인 스레드에서만 처리
     */
    private static void example10_5() {
        Flux.fromArray(new Integer[]{1, 3, 5, 7})
                .doOnNext(data -> log.info("# doOnNext fromArray : {}", data))
                .filter(data -> data > 3)
                .doOnNext(data -> log.info("# doOnNext filter : {}", data))
                .map(data -> data * 10)
                .doOnNext(data -> log.info("# doOnNext map : {}", data))
                .subscribe(data -> log.info("# onNext : {}", data));
    }

    /**
     * publishOn 사용, 다음 doOnNext에서 부터 다른 스레드로 변경
     */
    private static void example10_6() {
        Flux.fromArray(new Integer[]{1, 3, 5, 7})
                .doOnNext(data -> log.info("# doOnNext fromArray : {}", data))
                .publishOn(Schedulers.parallel())
                .filter(data -> data > 3)
                .doOnNext(data -> log.info("# doOnNext filter : {}", data))
                .map(data -> data * 10)
                .doOnNext(data -> log.info("# doOnNext map : {}", data))
                .subscribe(data -> log.info("# onNext : {}", data));
    }

    /**
     * publishOn 두번 용, 다음 doOnNext에서 부터 다른 스레드로 변경
     */
    private static void example10_7() throws InterruptedException {
        Flux.fromArray(new Integer[]{1, 3, 5, 7})
                .doOnNext(data -> log.info("# doOnNext fromArray : {}", data))
                .publishOn(Schedulers.parallel())
                .filter(data -> data > 3)
                .doOnNext(data -> log.info("# doOnNext filter : {}", data))
                .publishOn(Schedulers.parallel())
                .map(data -> data * 10)
                .doOnNext(data -> log.info("# doOnNext map : {}", data))
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(500L);
    }

    private static void example10_8() throws InterruptedException {
        Flux.fromArray(new Integer[]{1, 3, 5, 7})
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(data -> log.info("# doOnNext fromArray : {}", data))
                .filter(data -> data > 3)
                .doOnNext(data -> log.info("# doOnNext filter : {}", data))
                .publishOn(Schedulers.parallel())
                .map(data -> data * 10)
                .doOnNext(data -> log.info("# doOnNext map : {}", data))
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(500L);
    }

}
