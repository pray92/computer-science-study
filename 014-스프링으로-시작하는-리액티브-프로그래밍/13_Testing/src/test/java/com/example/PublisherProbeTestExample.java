package com.example;

import reactor.core.publisher.Mono;

public class PublisherProbeTestExample {

    public static Mono<String> processTask(Mono<String> main, Mono<String> standby) {
        return main.flatMap(message -> Mono.just(message))
                .switchIfEmpty(standby);
    }

    public static Mono<String> supplyMainPower() {
        return Mono.empty();
    }

    public static Mono<String> supplyStandbyPower() {
        return Mono.just("# supply Standby Power");
    }
}
