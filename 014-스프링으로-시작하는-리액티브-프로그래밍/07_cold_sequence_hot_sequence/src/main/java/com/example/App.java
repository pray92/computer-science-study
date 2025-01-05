package com.example;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;

public class App {
    public static void main(String[] args) throws InterruptedException {
        example7_3();
    }

    private static void example7_1() throws InterruptedException {
        Flux<String> coldFlux = Flux.fromIterable(Arrays.asList("KOREA", "JAPAN", "CHINA"))
                                .map(String::toLowerCase);

        coldFlux.subscribe(country -> System.out.println("# Subscriber1: " + country));
        System.out.println("---------------------------------");
        Thread.sleep(2000L);
        coldFlux.subscribe(country -> System.out.println("# Subscriber2: " + country));
    }

    // Hot Sequence 예제
    private static void example7_2() throws InterruptedException {
        String[] singers = {"Singer A","Singer B","Singer C","Singer D","Singer E"};

        System.out.println("# Begin convert: ");
        Flux<String> concertFlux = Flux.fromArray(singers)
                                        .delayElements(Duration.ofSeconds(1))
                                        .share();

        // share() : 원본 Flux를 멀티캐스트(또는 공유)하는 새로운 Flux를 리턴
        // 원본 Flux라는 것은 Operator를 통해 미가공된, 원본 데이터 소스를 처음으로 Emit하는 Flux

        concertFlux.subscribe(singer -> System.out.println("# Subscriber1 is watching " + singer + "'s song"));

        Thread.sleep(2500);

        concertFlux.subscribe(singer -> System.out.println("# Subscriber2 is watching " + singer + "'s song"));

        Thread.sleep(3000);
    }

    /**
     * HTTP 요청과 응답에서 Cold Sequence와 Hot Sequence의 동작 흐름
     */
    private static void example7_3() throws InterruptedException {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("worldtimeapi.org")
                .path("/api/timezone/Asia/Seoul")
                .build()
                .encode()
                .toUri();

        // cache() : Cold Sequence로 동작하는 Mono를 Hot Sequence로 변경해주고 emit된 데이터를 캐시한 뒤,
        // 구독이 발생할 때마다 캐시된 데이터를 전달 -> 결과적으로 구독 발생 시 동일한 데이터를 전달받게 됨
        Mono<String> mono = getWorldTime(uri).cache();
        mono.subscribe(dateTime -> System.out.println("# dateTime 1 : " + dateTime));
        Thread.sleep(2000);
        mono.subscribe(dateTime -> System.out.println("# dateTime 2 : " + dateTime));
        Thread.sleep(2000);
    }

    private static Mono<String> getWorldTime(URI worldTimeUri) {
        return WebClient.create()
                .get()
                .uri(worldTimeUri)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    DocumentContext jsonContext = JsonPath.parse(response);
                    String dateTime = jsonContext.read("$.datetime");
                    return dateTime;
                });
    }
}
