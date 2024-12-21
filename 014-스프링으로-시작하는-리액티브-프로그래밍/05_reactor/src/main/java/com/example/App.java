package com.example;

import reactor.core.publisher.Flux;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Flux<String> sequence = Flux.just("Hello", "Reactor");
        sequence.map(String::toLowerCase)
                .subscribe(System.out::println);
    }
}
