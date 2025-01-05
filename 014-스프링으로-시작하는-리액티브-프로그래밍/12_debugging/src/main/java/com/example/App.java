package com.example;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Slf4j
public class App {
    public static void main(String[] args) throws InterruptedException {
        example12_7();
    }

    public static Map<String, String> fruits = new HashMap<>();
    static {
        fruits.put("banana", "바나나");
        fruits.put("apple", "사과");
        fruits.put("pear", "배");
        fruits.put("grape", "포도");
    }

    private static void example12_1() throws InterruptedException {
        Hooks.onOperatorDebug();

        Flux.fromArray(new String[]{"BANANAS", "APPLES", "PEARS", "MELONS"})
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .map(String::toLowerCase)
                .map(fruit -> fruit.substring(0, fruit.length() - 1))
                .map(fruits::get)
                .map(translated -> "맛있는 " + translated)
                .subscribe(log::info, error -> log.error("# onError : {}", error));

        Thread.sleep(100L);
    }

    private static void example12_2() {
        Flux.just(2, 4, 6, 8)
                .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y)
                .map(num -> num + 2)
                .checkpoint()
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError : {}", error)
                );
    }

    private static void example12_3() {
        Flux.just(2, 4, 6, 8)
                .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y)
                .checkpoint()
                .map(num -> num + 2)
                .checkpoint()
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError : {}", error)
                );
    }

    private static void example12_4() {
        Flux.just(2, 4, 6, 8)
                .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y)
                .checkpoint("Example12_4.zipWith.checkpoint")
                .map(num -> num + 2)
                .checkpoint("Example12_4.map.checkpoint")
                .subscribe(
                        data -> log.info("# onNext : {}", data),
                        error -> log.error("# onError : {}", error)
                );
    }

    private static void example12_5() {
        Flux.just(2, 4, 6, 8)
                .zipWith(Flux.just(1, 2, 3, 0), (x, y) -> x / y)
                .checkpoint("Example12_4.zipWith.checkpoint", true)
                .map(num -> num + 2)
                .checkpoint("Example12_4.map.checkpoint", true)
                .subscribe(
                        data -> log.info("# onNext : {}", data),
                        error -> log.error("# onError : {}", error)
                );
    }

    private static void example12_6() {
        Flux<Integer> source = Flux.just(2, 4, 6, 8);
        Flux<Integer> other = Flux.just(1, 2, 3, 0);

        Flux<Integer> divideSource = divide(source, other).checkpoint();
        Flux<Integer> plusSource = plus(divideSource).checkpoint();

        plusSource.subscribe(
                data -> log.info("# onNext : {}", data),
                error -> log.error("# onError : {}", error)
        );
    }

    private static Flux<Integer> divide(Flux<Integer> source, Flux<Integer> other) {
        return source.zipWith(other, (x, y) -> x / y);
    }

    private static Flux<Integer> plus(Flux<Integer> source) {
        return source.map(num -> num + 2);
    }

    private static void example12_7() {
        Flux.fromArray(new String[]{"BANANAS", "APPLES", "MELONS", "PEARS"})
                .map(String::toLowerCase)
                .map(fruit -> fruit.substring(0, fruit.length() - 1))
                .log("Fruit.subString", Level.FINE)
                .map(fruits::get)
                .subscribe(
                        log::info,
                        error -> log.error("# onError : {}", error)
                );
    }

}
