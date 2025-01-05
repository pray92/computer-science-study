package com.example;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.Disposable;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.math.MathFlux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.DataFormatException;

@Slf4j
public class App {
    public static void main(String[] args) throws InterruptedException, IOException {
        example14_63();
    }

    private static void example14_1() {
        Mono.justOrEmpty(null)
                .subscribe(data -> {},
                        error -> {},
                        () -> log.info("# onComplete"));
    }

    private static void example14_2() {
        Flux.fromIterable(SampleData.coins)
                .subscribe(coin -> log.info("coin 명 : {}, 현재가 : {}", coin.getT1(), coin.getT2()));
    }

    private static void example14_3() {
        Flux.fromStream(() -> SampleData.coinNames.stream())
                .filter(coin -> coin.equals("BTC") || coin.equals("ETH"))
                .subscribe(data -> log.info("{}", data));
    }

    private static void example14_4() {
        Flux.range(5, 10).subscribe(data -> log.info("{}", data));
    }

    private static void example14_5() {
        Flux.range(7, 5)
                .map(idx -> SampleData.btcTopPricesPerYear.get(idx))
                .subscribe(tuple -> log.info("{}'s {}", tuple.getT1(), tuple.getT2()));
    }

    private static void example14_6() throws InterruptedException {
        log.info("# start : {}", LocalDateTime.now());
        Mono<LocalDateTime> justMono = Mono.just(LocalDateTime.now());
        Mono<LocalDateTime> deferMono = Mono.defer(() -> Mono.just(LocalDateTime.now()));

        Thread.sleep(2000L);

        justMono.subscribe(data -> log.info("# onNext just1 : {}", data));
        deferMono.subscribe(data -> log.info("# onNext defer1 : {}", data));

        Thread.sleep(2000L);

        justMono.subscribe(data -> log.info("# onNext just2 : {}", data));
        deferMono.subscribe(data -> log.info("# onNext defer2 : {}", data));
    }

    private static void example14_7() throws InterruptedException {
        log.info("# start: {}", LocalDateTime.now());
        Mono.just("Hello")
                .delayElement(Duration.ofSeconds(3))
                //.switchIfEmpty(sayDefault())
                .switchIfEmpty(Mono.defer(() -> sayDefault()))
                .subscribe(data -> log.info("# onNext: {}", data));
        Thread.sleep(3500L);
    }

    private static Mono<String> sayDefault() {
        log.info("# Say Hi");
        return Mono.just("Hi");
    }

    private static void example14_8() throws IOException {
        ClassPathResource resource = new ClassPathResource("using_example.txt");
        Path path = Paths.get(resource.getURI());

        Flux.using(() -> Files.lines(path), Flux::fromStream, Stream::close)
                .subscribe(log::info);
    }

    private static void example14_9() {
        Flux.generate(() -> 0, (state, sink) -> {
                    sink.next(state);
                    if (10 == state) {
                        sink.complete();
                    }
                    return ++state;
                })
                .subscribe(data -> log.info("# onNext: {}", data));
    }

    private static void example14_10() {
        final int dan = 3;

        Flux.generate(() -> Tuples.of(dan, 1), (state, sink) -> {
                    sink.next(state.getT1() + " * " + state.getT2() + " = " + state.getT1() * state.getT2());
                    if (9 == state.getT2()) {
                        sink.complete();
                    }
                    return Tuples.of(state.getT1(), state.getT2() + 1);
                }, state -> log.info("# 구구단 {}단 종료!", state.getT1()))
                .subscribe(data -> log.info("# onNext: {}", data));
    }

    private static void example14_11() {
        Map<Integer, Tuple2<Integer, Long>> map = SampleData.getBtcTopPricesPerYearMap();

        Flux.generate(() -> 2019, (state, sink) -> {
                    if (state > 2021) {
                        sink.complete();
                    } else {
                        sink.next(map.get(state));
                    }
                    return ++state;
                })
                .subscribe(data -> log.info("# onNext : {}", data));
    }

    static int SIZE = 0;
    static int COUNT = -1;
    final static List<Integer> DATA_SOURCE = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    private static void example14_12() {
        log.info("# start");
        Flux.create((FluxSink<Integer> sink) -> {
            sink.onRequest(n -> {
                try {
                    Thread.sleep(1000L);
                    for (int i = 0; i < n; ++i) {
                        if (COUNT >= 9) {
                            sink.complete();
                        } else {
                            COUNT++;
                            sink.next(DATA_SOURCE.get(COUNT));
                        }
                    }
                } catch (InterruptedException e) {}
            });

            sink.onDispose(() -> log.info("# clean up"));
        }).subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
            }

            @Override
            protected void hookOnNext(Integer value) {
                SIZE++;
                log.info("# onNext : {}", value);
                if(SIZE == 2) {
                    request(2);
                    SIZE = 0;
                }
            }

            @Override
            protected void hookOnComplete() {
                log.info("# onComplete");
            }
        });
    }

    private static void example14_13() throws InterruptedException {
        CryptoCurrencyPriceEmitter priceEmitter = new CryptoCurrencyPriceEmitter();

        Flux.create((FluxSink<Integer> sink) -> priceEmitter.setListener(new CryptoCurrencyPriceListener() {
                    @Override
                    public void onPrice(List<Integer> priceList) {
                        priceList.forEach(price -> sink.next(price));
                    }

                    @Override
                    public void onComplete() {
                        sink.complete();
                    }
                }))
                .publishOn(Schedulers.parallel())
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> {},
                        () -> log.info("# onComplete")
                );

        Thread.sleep(3000L);

        priceEmitter.flowInto();

        Thread.sleep(2000L);
        priceEmitter.complete();
    }

    static int start = 1;
    static int end = 4;

    private static void example14_14() throws InterruptedException {
        Flux.create((FluxSink<Integer> sink) -> {
                    sink.onRequest(n -> {
                        log.info("# requested : " + n);
                        try {
                            Thread.sleep(500L);
                            for (int i = start; i <= end; ++i) {
                                sink.next(i);
                            }
                            start += 4;
                            end += 4;
                        } catch (InterruptedException e) {
                        }
                    });

                    sink.onDispose(() -> {
                        log.info("# clean up");
                    });
                }, FluxSink.OverflowStrategy.DROP)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel(), 2)
                .subscribe(data -> log.info("# onNext: {}", data));

        Thread.sleep(3000L);
    }

    private static void example14_15() {
        Flux.range(1, 20)
                .filter(num -> num % 2 == 0)
                .subscribe(data -> log.info("# onNext : {}", data));
    }

    private static void example14_16() {
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .filter(tuple -> tuple.getT2() > 20_000_000)
                .subscribe(data -> log.info("{}:{}", data.getT1(), data.getT2()));
    }

    private static void example14_17() throws InterruptedException {
        Map<SampleData.CovidVaccine, Tuple2<SampleData.CovidVaccine, Integer>> vaccineMap = SampleData.getCovidVaccines();

        Flux.fromIterable(SampleData.coronaVaccineNames)
                .filterWhen(vaccine -> Mono.just(vaccineMap.get(vaccine).getT2() >= 3_000_000)
                        .publishOn(Schedulers.parallel()))
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(1000L);
    }

    private static void example14_18() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .skip(2)
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(5000L);
    }

    private static void example14_19() throws InterruptedException {
        Flux.interval(Duration.ofMillis(300))
                .skip(Duration.ofSeconds(1))
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(2000L);
    }

    private static void example14_20() {
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .filter(tuple -> tuple.getT2() >= 20_000_000)
                .skip(2)
                .subscribe(tuple -> log.info("{}:{}", tuple.getT1(), tuple.getT2()));
    }

    private static void example14_21() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .take(3)
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(4000L);
    }

    private static void example14_22() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .take(Duration.ofMillis(2500))
                .subscribe(data -> log.info("# onNext : {}", data));

        Thread.sleep(3000L);
    }

    private static void example14_23() {
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .takeLast(2)
                .subscribe(tuple -> log.info("# onNext: {}, {}", tuple.getT1(), tuple.getT2()));
    }

    private static void example14_24() {
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .takeUntil(tuple -> tuple.getT2() > 20_000_000)
                .subscribe(tuple -> log.info("# onNext: {}, {}", tuple.getT1(), tuple.getT2()));
    }

    private static void example14_25() {
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .takeWhile(tuple -> tuple.getT2() < 20_000_000)
                .subscribe(tuple -> log.info("# onNext: {}, {}", tuple.getT1(), tuple.getT2()));
    }

    private static void example14_26() {
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .next()
                .subscribe(tuple -> log.info("# onNext: {}, {}", tuple.getT1(), tuple.getT2()));
    }

    private static void example14_27() {
        Flux.just("1-Circle", "3-Circle", "5-Circle")
                .map(circle -> circle.replace("Circle", "Rectangle"))
                .subscribe(data -> log.info("# onNext: {}", data));
    }

    private static void example14_28() {
        final double buyPrice = 50_000_000;
        Flux.fromIterable(SampleData.btcTopPricesPerYear)
                .filter(tuple -> tuple.getT1() == 2021)
                .doOnNext(data -> log.info("# doOnNext: {}", data))
                .map(tuple -> calculateProfitRate(buyPrice, tuple.getT2()))
                .subscribe(data -> log.info("# onNext: {}%", data));
    }

    private static double calculateProfitRate(final double buyPrice, Long topPrice) {
        return (topPrice - buyPrice) / buyPrice * 100;
    }

    private static void example14_29() {
        Flux.just("Good", "Bad")
                .flatMap(feeling -> Flux.just("Morning", "Afternoon", "Evening")
                        .map(time -> feeling + " " + time))
                .subscribe(log::info);
    }

    private static void example14_30() throws InterruptedException {
        Flux.range(2, 8)
                .flatMap(dan -> Flux.range(1, 9)
                        .publishOn(Schedulers.parallel())
                        .map(n -> dan + " * " + n + " = " + dan * n))
                .subscribe(log::info);

        Thread.sleep(100L);
    }

    private static void example14_31() {
        Flux.concat(Flux.just(1, 2, 3), Flux.just(4, 5))
                .subscribe(data -> log.info("# onNext : {}", data));
    }

    private static void example14_32() {
        Flux.concat(Flux.fromIterable(SampleData.viralVectorVaccines),
                        Flux.fromIterable(SampleData.mRNAVaccines),
                        Flux.fromIterable(SampleData.subunitVaccines))
                .subscribe(data -> log.info("# onNext: {}", data));
    }

    private static void example14_33() throws InterruptedException {
        Flux.merge(Flux.just(1, 2, 3, 4).delayElements(Duration.ofMillis(300L)),
                Flux.just(5, 6, 7).delayElements(Duration.ofMillis(500L)))
                .subscribe(data -> log.info("# onNext: {}", data));
        Thread.sleep(2000L);
    }

    private static void example14_34() throws InterruptedException {
        String[] usaStates = {
                "Ohio", "Michigan", "New Jersey", "Illinois", "New Hampshire",
                "Virginia", "Vermont", "North Carolina", "Ontario", "Georgia"
        };

        Flux.merge(getMeltDownRecoveryMessage(usaStates))
                .subscribe(log::info);

        Thread.sleep(2000L);
    }

    private static List<Mono<String>> getMeltDownRecoveryMessage(String[] usaStates) {
        List<Mono<String>> messages = new ArrayList<>();
        for (String state : usaStates) {
            messages.add(SampleData.nppMap.get(state));
        }
        return messages;
    }

    private static void example14_35() throws InterruptedException {
        Flux.zip(Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
                Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L)))
                .subscribe(tuple2 -> log.info("# onNext: {}", tuple2));

        Thread.sleep(2500L);
    }

    private static void example14_36() throws InterruptedException {
        Flux.zip(Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300L)),
                Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500L)),
                (n1, n2) -> n1 * n2)
                .subscribe(tuple2 -> log.info("# onNext: {}", tuple2));

        Thread.sleep(2500L);
    }

    private static void example14_37() {
        getInfectedPersonsPerHour(10, 21)
                .subscribe(tuples -> {
                    Tuple3<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> t3 = tuples;
                    int sum = t3.getT1().getT2() + t3.getT2().getT2() + t3.getT3().getT2();
                    log.info("# onNext: {}, {}", t3.getT1().getT1(), sum);
                });
    }

    private static Flux<Tuple3<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>> getInfectedPersonsPerHour(int start, int end) {
        return Flux.zip(Flux.fromIterable(SampleData.seoulInfected).filter(t2 -> t2.getT1() >= start && t2.getT1() <= end),
                Flux.fromIterable(SampleData.incheonInfected).filter(t2 -> t2.getT1() >= start && t2.getT1() <= end),
                Flux.fromIterable(SampleData.suwonInfected).filter(t2 -> t2.getT1() >= start && t2.getT1() <= end));
    }

    private static void example14_38() throws InterruptedException {
        Mono.just("Task 1")
                .delayElement(Duration.ofSeconds(1))
                .doOnNext(data -> log.info("# Mono doOnNext : {}", data))
                .and(Flux.just("Task 2", "Task3")
                        .delayElements(Duration.ofMillis(600))
                        .doOnNext(data -> log.info("# FluxdoOnNext : {}", data))
                )
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError : {}", error),
                        () -> log.info("# onComplete")
                );

        Thread.sleep(5000);
    }

    private static void example14_39() throws InterruptedException {
        restartApplicationServer()
                .and(restartDbServer())
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError: {}", error),
                        () -> log.info("# sent an email to Administrator: All Servers are restarted successfully")
                );

        Thread.sleep(6000L);
    }

    private static Mono<String> restartApplicationServer() {
        return Mono.just("Application Server was restartedd successfully")
                .delayElement(Duration.ofSeconds(2))
                .doOnNext(log::info);
    }

    private static Publisher<String> restartDbServer() {
        return Mono.just("DB Server was restarted successfully")
                .delayElement(Duration.ofSeconds(4))
                .doOnNext(log::info);
    }

    private static void example14_40() {
        Flux.just("...", "---", "...")
                .map(code -> transformMorseCode(code))
                .collectList()
                .subscribe(list -> log.info(list.stream().collect(Collectors.joining())));
    }

    private static String transformMorseCode(String morseCode) {
        return SampleData.morseCodeMap.get(morseCode);
    }

    private static void example14_41() {
        Flux.range(0, 26)
                .collectMap(key -> SampleData.morseCodes[key],
                        value -> transformToLetter(value))
                .subscribe(map -> log.info("# onNext: {}", map));
    }

    private static String transformToLetter(int value) {
        return Character.toString((char)('a' + value));
    }

    private static void example14_42() {
        Flux.range(1, 5)
                .doFinally(signalType -> log.info("# doFinally 1: {}", signalType))
                .doFinally(signalType -> log.info("# doFinally 2: {}", signalType))
                .doOnNext(data -> log.info("# range > doOnNext(): {}", data))
                .doOnRequest(data -> log.info("# doOnRequest: {}", data))
                .doOnSubscribe(subscription -> log.info("# doOnSubscribe 1"))
                .doFirst(() -> log.info("# doFirst()"))
                .filter(num -> num % 2 == 0)
                .doOnNext(data -> log.info("# filter > doOnNext(): {}", data))
                .doOnComplete(() -> log.info("# doOnComplete()"))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("# hookOnNext: {}", value);
                        request(1);
                    }
                });
    }

    private static void example14_43() {
        Flux.range(1, 5)
                .flatMap(num -> {
                    if ((num * 2) % 3 == 0) {
                        return Flux.error(new IllegalArgumentException("Not allowed multiple of 3"));
                    } else {
                        return Mono.just(num * 2);
                    }
                })
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError: {}",error)
                );
    }

    private static void example14_44() {
        Flux.just('a', 'b', '3', 'd', 'e')
                .flatMap(letter -> {
                    try {
                        return convert(letter);
                    } catch (DataFormatException e) {
                        return Flux.error(e);
                    }
                })
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError: ", error)
                );
    }

    private static Mono<String> convert(char ch) throws DataFormatException {
        if (!Character.isAlphabetic(ch)) {
            throw new DataFormatException("Not Alphabetic");
        }
        return Mono.just("Converted to " + Character.toUpperCase(ch));
    }

    private static void example14_45() {
        getBooks().map(book -> book.getPenName().toUpperCase())
                .onErrorReturn("No pen name")
                .subscribe(log::info);
    }

    private static Flux<Book> getBooks() {
        return Flux.fromIterable(SampleData.books);
    }

    private static void example14_46() {
        getBooks().map(book -> book.getPenName().toUpperCase())
                .onErrorReturn(NullPointerException.class, "no pen name")
                .onErrorReturn(IllegalFormatException.class, "Illegal pen name")
                .subscribe(log::info);
    }

    private static void example14_47() {
        final String keyword = "DDD";
        getBooksFromCache(keyword)
                .onErrorResume(error -> getBooksFromDatabase(keyword))
                .subscribe(
                        data -> log.info("# onNext: {}", data.getBookName()),
                        error -> log.error("# onError: ", error)
                );
    }

    private static Flux<Book> getBooksFromDatabase(final String keyword) {
        List<Book> books = new ArrayList<>(SampleData.books);
        books.add(new Book("DDD: Domain Driven Design", "Joy", "ddd-man", 35000, 200));
        return Flux.fromIterable(books)
                .filter(book -> book.getBookName().contains(keyword))
                .switchIfEmpty(Flux.error(new NoSuchBookException("No such Book")));
    }

    private static Flux<Book> getBooksFromCache(final String keyword) {
        return Flux.fromIterable(SampleData.books)
                .filter(book -> book.getBookName().contains(keyword))
                .switchIfEmpty(Flux.error(new NoSuchBookException("No such Book")));
    }

    private static class NoSuchBookException extends RuntimeException {
        NoSuchBookException(String message) {
            super(message);
        }
    }

    private static void example14_48() {
        Flux.just(1, 2, 4, 0, 6, 12)
                .map(num -> 12 / num)
                .onErrorContinue((error, num) -> log.error("error: {}, num: {}", error.getMessage(), num))
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError: ", error)
                );
    }

    private static void example14_49() throws InterruptedException {
        final int[] count = {1};
        Flux.range(1, 3)
                .delayElements(Duration.ofSeconds(1))
                .map(num -> {
                    try {
                        if (num == 3 && count[0] == 1) {
                            count[0]++;
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {}

                    return num;
                })
                .timeout(Duration.ofMillis(1500))
                .retry(1)
                .subscribe(
                        data -> log.info("# onNext: {}", data),
                        error -> log.error("# onError: ", error),
                        () -> log.info("# onComplete")
                );

        Thread.sleep(7000);
    }

    private static void example14_50() throws InterruptedException {
        getBooks()
                .collect(Collectors.toSet())
                .subscribe(bookSet -> bookSet.stream()
                        .forEach(book -> log.info("book name: {}, price: {}",
                                book.getBookName(), book.getPrice())));

        Thread.sleep(12000);
    }

    private static void example14_51() throws InterruptedException {
        Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .elapsed()
                .subscribe(data -> log.info("# onNext: {}, time: {}", data.getT2(), data.getT1()));

        Thread.sleep(6000);
    }

    private static void example14_52() {
        URI worldTimeUri = UriComponentsBuilder.newInstance().scheme("https")
                .host("worldtimeapi.org")
                .path("/api/timezone/Asia/Seoul")
                .build()
                .encode()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Mono.defer(() -> Mono.just(restTemplate.exchange(worldTimeUri, HttpMethod.GET, new HttpEntity<>(headers), String.class)))
                .repeat(4)
                .elapsed()
                .map(response -> {
                    DocumentContext jsonContext = JsonPath.parse(response.getT2().getBody());
                    String dateTime = jsonContext.read("$.datetime");
                    return Tuples.of(dateTime, response.getT1());
                })
                .subscribe(
                        data -> log.info("now: {}, elapsed: {}", data.getT1(), data.getT2()),
                        error -> log.error("# onError: ", error),
                        () -> log.info("# onComplete")
                );
    }

    private static void example14_53() {
        Flux.range(1, 11)
                .window(3)
                .flatMap(flux -> {
                    log.info("==================");
                    return flux;
                })
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        subscription.request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("# onNext: {}", value);
                        request(2);
                    }
                });
    }

    private static void example14_54() {
        Flux.fromIterable(SampleData.monthlyBookSales2021)
                .window(3)
                .flatMap(flux -> MathFlux.sumInt(flux))
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        subscription.request(2);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        log.info("# onNext: {}", value);
                        request(2);
                    }
                });
    }

    private static void example14_55() {
        Flux.range(1, 95)
                .buffer(10)
                .subscribe(buffer -> log.info("# onNext: {}", buffer));
    }

    private static void example14_56() {
        Flux.range(1, 20)
                .map(num -> {
                    try {
                        if (num < 10) {
                            Thread.sleep(100L);
                        } else {
                            Thread.sleep(300L);
                        }
                    } catch (InterruptedException e) {
                    }
                    return num;
                })
                .bufferTimeout(3, Duration.ofMillis(400L))
                .subscribe(buffer -> log.info("# onNext: {}", buffer));
    }

    private static void example14_57() {
        Flux.fromIterable(SampleData.books)
                .groupBy(Book::getAuthorName)
                .flatMap(groupedFlux ->
                        groupedFlux.map(book -> book.getBookName() + "(" + book.getAuthorName() + ")").collectList()
                )
                .subscribe(bookByAuthor -> log.info("# book by author: {}", bookByAuthor));
    }

    private static void example14_58() {
        Flux.fromIterable(SampleData.books)
                .groupBy(Book::getAuthorName,
                        book -> book.getBookName() + "(" + book.getAuthorName() + ")")
                .flatMap(Flux::collectList)
                .subscribe(bookByAuthor -> log.info("# book by author: {}", bookByAuthor));
    }

    private static void example14_59() {
        Flux.fromIterable(SampleData.books)
                .groupBy(Book::getAuthorName)
                .flatMap(groupedFlux -> Mono.just(groupedFlux.key())
                        .zipWith(groupedFlux.map(book -> (int) (book.getPrice() * book.getStockQuantity() * 0.1))
                                        .reduce((y1, y2) -> y1 + y2),
                                (authorName, sumRoyalty) -> authorName + "'s royalty : " + sumRoyalty))
                .subscribe(log::info);
    }

    private static void example14_60() throws InterruptedException {
        ConnectableFlux<Integer> flux = Flux.range(1, 5)
                .delayElements(Duration.ofMillis(300L))
                .publish();

        Thread.sleep(500L);
        flux.subscribe(data -> log.info("# subscriber1: {}", data));

        Thread.sleep(200L);
        flux.subscribe(data -> log.info("# subscriber2: {}", data));

        flux.connect();

        Thread.sleep(1000L);
        flux.subscribe(data -> log.info("# subscriber3: {}", data));

        Thread.sleep(2000L);
    }

    private static ConnectableFlux<String> publisher;
    private static int checkedAudience;
    static {
        publisher = Flux.just("Concert part1", "Concert part2", "Concert part3")
                .delayElements(Duration.ofMillis(300L))
                .publish();
    }

    private static void example14_61() throws InterruptedException {
        checkAudience();
        // connect 시점부터 발행(publish)하기 때문에 2개의 sleep은 의미 없음
        // 해당 코드의 목적은 connect 시점부터 Flux가 데이터를 발행하는 것을 보여주기 위함
        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 1 is watching {}", data));
        checkedAudience++;

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 2 is watching {}", data));
        checkedAudience++;

        checkAudience();

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 3 is watching {}", data));

        Thread.sleep(1000L);
    }

    private static void checkAudience() {
        if(checkedAudience >= 2) {
            publisher.connect();
        }
    }

    private static void example14_62() throws InterruptedException {
        Flux<String> publisher = Flux.just("Concert part1", "Concert part2", "Concert part3")
                .delayElements(Duration.ofMillis(300L))
                .publish()
                .autoConnect(2);

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 1 is watching {}", data));

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 2 is watching {}", data));

        Thread.sleep(500L);
        publisher.subscribe(data -> log.info("# audience 3 is watching {}", data));

        Thread.sleep(1000L);
    }

    private static void example14_63() throws InterruptedException {
        Flux<Long> publisher = Flux.interval(Duration.ofMillis(500))
                //.publish().autoConnect(1)
                .publish().refCount(1);

        Disposable disposable = publisher.subscribe(data -> log.info("# subscriber 1 : {}", data));

        Thread.sleep(2100L);
        disposable.dispose();

        publisher.subscribe(data -> log.info("# subscriber 2 : {}", data));

        Thread.sleep(2500L);

    }

}
