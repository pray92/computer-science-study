package com.example;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.StepVerifierOptions;
import reactor.test.publisher.PublisherProbe;
import reactor.test.publisher.TestPublisher;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppTest {

    @Test
    public void sayHelloReactorTest() {
        StepVerifier.create(Mono.just("Hello Reactor"))         // 테스트 대상 Sequence 생성
                .expectNext("Hello Reactor")                       // emit된 데이터 기댓값 평가
                .expectComplete()                                     // onComplete Signal 기댓값 평가
                .verify();                                            // 검증 실행
    }

    @Test
    void sayHelloTest() {
        StepVerifier.create(GeneralTestExample.sayHello())
                .expectSubscription()
                .as("# expect subscription")
                .expectNext("Hi")
                .as("# expect Hi")
                .expectNext("Reactor")
                .as("# expect Reactor")
                .verifyComplete();
    }

    @Test
    void divideByTwoTest() {
        Flux<Integer> source = Flux.just(2, 4, 6, 8, 10);
        StepVerifier.create(GeneralTestExample.divideByTwo(source))
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                //.expectNext(1, 2, 3, 4)
                .expectError()
                .verify();
    }

    @Test
    void takeNumberTest() {
        Flux<Integer> source = Flux.range(0, 1000);
        StepVerifier.create(GeneralTestExample.takeNumber(source, 500),
                            StepVerifierOptions.create().scenarioName("Verify from 0 to 499"))
                .expectSubscription()
                .expectNext(0)
                .expectNextCount(498)
                .expectNext(500)
                .expectComplete()
                .verify();
    }

    @Test
    void getCOVID19CountTest() {
        StepVerifier.withVirtualTime(() -> TimeBasedTestExample.getCOVID19Count(
                                Flux.interval(Duration.ofMinutes(1)).take(1)
                        )
                )
                .expectSubscription()
                /*.then(() -> VirtualTimeScheduler.get().advanceTimeBy(Duration.ofHours(1)))*/
                .expectNextCount(11)
                .expectComplete()
                .verify(Duration.ofSeconds(3));
    }

    @Test
    void getVoteCountTest() {
        StepVerifier.withVirtualTime(() -> TimeBasedTestExample.getVoteCount(Flux.interval(Duration.ofMinutes(1))))
                .expectSubscription()
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNoEvent(Duration.ofMinutes(1))
                .expectNextCount(5)
                .expectComplete()
                .verify();
    }

    @Test
    void generateNumberTest() {
        StepVerifier.create(BackpressureTestExample.generateNumber(), 1L)
                .thenConsumeWhile(num -> num >= 1)
                .expectError()
                .verifyThenAssertThat()
                .hasDroppedElements();

    }

    @Test
    void getSecretMessageTest() {
        Mono<String> source = Mono.just("hello");

        StepVerifier.create(ContextTestExample.getSecretMessage(source)
                        .contextWrite(context -> context.put("secretMessage", "Hello, Reactor"))
                        .contextWrite(context -> context.put("secretKey", "aGVsbG8="))
                )
                .expectSubscription()
                .expectAccessibleContext()
                .hasKey("secretKey")
                .hasKey("secretMessage")
                .then()
                .expectNext("Hello, Reactor")
                .expectComplete()
                .verify();
    }

    @Test
    void getCityTest() {
        StepVerifier.create(RecordTestExample.getCapitalizedCountry(
                        Flux.just("korea", "england", "canada", "india")
                ))
                .expectSubscription()
                .recordWith(ArrayList::new)
                .thenConsumeWhile(country -> !country.isEmpty())
                .expectRecordedMatches(countries ->
                        countries.stream().allMatch(country -> Character.isUpperCase(country.charAt(0)))
                )
                .expectComplete()
                .verify();
    }

    @Test
    void divideByTwoTestWIthTestPublisher() {
        TestPublisher<Integer> source = TestPublisher.create();

        StepVerifier.create(GeneralTestExample.divideByTwo(source.flux()))
                .expectSubscription()
                .then(() -> source.emit(2, 4, 6, 8, 10))
                .expectNext(1, 2, 3, 4)
                .expectError()
                .verify();

    }

    @Test
    void divideByTwoTestWithMisbehavingTestPublisher() {
        //TestPublisher<Integer> source = TestPublisher.create();
        TestPublisher<Integer> source = TestPublisher.createNoncompliant(TestPublisher.Violation.ALLOW_NULL);

        StepVerifier.create(GeneralTestExample.divideByTwo(source.flux()))
                .expectSubscription()
                .then(() -> {
                    getDataSource().forEach(source::next);
                    source.complete();
                })
                .expectNext(1, 2, 3, 4, 5)
                .expectComplete()
                .verify();
    }

    private List<Integer> getDataSource() {
        return Arrays.asList(2, 4, 6, 8, null);
    }

    @Test
    void publisherProbeTest() {
        PublisherProbe<String> probe = PublisherProbe.of(PublisherProbeTestExample.supplyStandbyPower());

        StepVerifier.create(PublisherProbeTestExample.processTask(PublisherProbeTestExample.supplyMainPower(), probe.mono()))
                .expectNextCount(1)
                .verifyComplete();

        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }
}
