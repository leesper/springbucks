package com.leesper.springbucksredismongo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootApplication
@Slf4j
public class SpringbucksRedismongoApplication implements ApplicationRunner {
	private final String KEY = "COFFEE_MENU";
    @Autowired
	private JdbcTemplate jdbcTemplate;
    @Autowired
	private ReactiveStringRedisTemplate redisTemplate;
    @Autowired
	private ReactiveMongoTemplate mongoTemplate;
    private CountDownLatch cdl2 = new CountDownLatch(2);

	@Bean
	public ReactiveStringRedisTemplate buildReactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
		return new ReactiveStringRedisTemplate(connectionFactory);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringbucksRedismongoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		reactiveRedisDemo();
		reactiveMongoDemo();
	}

	private void reactiveRedisDemo() throws Exception {
		List<Coffee> items = jdbcTemplate.query("SELECT * FROM t_coffee", (rs, ind) ->
			Coffee.builder()
					.id(rs.getString("id"))
					.name(rs.getString("name"))
					.price(rs.getLong("price"))
					.build()
		);

		CountDownLatch cdl = new CountDownLatch(1);
		ReactiveHashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();

		Flux.fromIterable(items)
				.publishOn(Schedulers.single())
				.doOnComplete(() -> log.info("list ok"))
				.flatMap(c -> {
					log.info("try to put {}: {}", c.getName(), c.getPrice());
					return opsForHash.put(KEY, c.getName(), c.getPrice().toString());
				})
				.doOnComplete(() -> log.info("set ok"))
				.concatWith(redisTemplate.expire(KEY, Duration.ofMinutes(1)))
				.doOnComplete(() -> log.info("expire ok"))
				.onErrorResume(e -> {
					log.error("Error: {}", e);
					return Mono.just(false);
				})
				.subscribe(b -> log.info("Result: {}", b),
						e -> log.error("Exception: {}", e),
						() -> cdl.countDown());

		log.info("Waiting");
		cdl.await();

	}

	private void reactiveMongoDemo() {
		startFromInsertion(() -> {
			log.info("Runnable");
			decreaseHighPrice();
		});

		log.info("after starting");
	}

	private List<Coffee> initCoffee() {
		Coffee latte = Coffee.builder()
				.name("latte")
				.price(3000L)
				.build();
		Coffee espresso = Coffee.builder()
				.name("espresso")
				.price(2000L)
				.build();
		return Arrays.asList(latte, espresso);
	}

	private void startFromInsertion(Runnable runnable) {
		mongoTemplate.insertAll(initCoffee())
				.publishOn(Schedulers.elastic())
				.doOnNext(c -> log.info("Processing {}", c))
				.doOnComplete(runnable)
				.doFinally(s -> {
					cdl2.countDown();
					log.info("Signal {}", s);
				})
				.count()
				.subscribe(c -> log.info("Inserted {} records", c));
	}

	private void decreaseHighPrice() {
		mongoTemplate.updateMulti(query(where("price").gte(3000L)),
				new Update().inc("price", -500L), Coffee.class)
				.doFinally(signalType -> {
					log.info("Singal: {}", signalType);
					cdl2.countDown();
				})
				.subscribe(updateResult -> log.info("Result: {}", updateResult));
	}
}
