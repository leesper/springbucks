package com.leesper.springbucksreactor;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.currentThread;

@SpringBootApplication
@Slf4j
public class SpringbucksReactorApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbucksReactorApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		backpressureDemo();
//		operatorsDemo();
//		combineTwoStreamsDemo();
		multiThreadDemo();
	}

	private void backpressureDemo() {
		List<Integer> elements = new ArrayList<>();
		Flux.range(1, 9)
				.log()
                .subscribe(new BaseSubscriber<Integer>() {
                	private Subscription s;
					@Override
					protected void hookOnSubscribe(Subscription subscription) {
					   s = subscription;
					   s.request(3);
					}

					@Override
					protected void hookOnNext(Integer value) {
						elements.add(value);
						if (elements.size() % 3 == 0) {
							s.request(3);
						}
					}

					@Override
					protected void hookOnComplete() {
						log.info("Complete");
					}

					@Override
					protected void hookOnError(Throwable throwable) {
						log.error("Error: {}", throwable);
					}
				});

		log.info("Elements: {}", elements);
	}

	private void operatorsDemo() {
		List<Integer> elements = new ArrayList<>();
		Flux.range(1, 6)
				.map(i -> i * 3)
				.subscribe(elements::add);
		log.info("operatorsDemo: {}", elements);
	}

	private void combineTwoStreamsDemo() {
		List<String> elements = new ArrayList<>();
		Flux.range(1, 3)
				.log()
				.zipWith(Flux.just("Linux Torvalds", "Turing", "Bill Gates"),
						(first, second) -> String.format("%d - %s", first, second))
				.subscribe(elements::add);
		log.info("combineTwoSteamasDemo: {}", elements);
	}
	private void multiThreadDemo() {
		Flux.range(1, 6)
				.log()
				.doOnRequest(n -> log.info("Request {}", n))
				.publishOn(Schedulers.elastic())
				.doOnComplete(() -> log.info("Complete 1"))
				.map(i -> {
					log.info("Publish {}: {}", currentThread(), i);
					return i * 2;
				})
				.doOnComplete(() -> log.info("Complete 2"))
				.publishOn(Schedulers.single())
				.map(i -> i - 1)
				.subscribe(i -> log.info("Subscribe {}: {}", currentThread(), i),
						e -> log.error("Error: {}", e),
						() -> log.info("Subscriber COMPLETE"));

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
