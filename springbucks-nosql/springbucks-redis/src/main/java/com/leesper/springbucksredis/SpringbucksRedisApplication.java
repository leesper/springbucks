package com.leesper.springbucksredis;

import com.leesper.springbucksredis.model.Coffee;
import com.leesper.springbucksredis.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
@EnableTransactionManagement
@EnableCaching(proxyTargetClass=true)
public class SpringbucksRedisApplication implements ApplicationRunner {
	@Autowired
	private JedisPoolConfig jedisPoolConfig;
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private CoffeeService coffeeService;

	private final String MENU = "springbucks-menu";

	public static void main(String[] args) {
		SpringApplication.run(SpringbucksRedisApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
//		jedisDemo();
//		cacheDemo();
		redisTemplateDemo();
	}

	@Bean
	@ConfigurationProperties(prefix = "redis")
	public JedisPoolConfig buildPoolConfig() {
		return new JedisPoolConfig();
	}

	@Bean(destroyMethod = "close")
	public JedisPool buildJedisPool(@Value("${redis.host}") String host) {
		return new JedisPool(buildPoolConfig(), host);
	}

	@Bean
	public RedisTemplate<Long, Coffee> buildRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<Long, Coffee> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}

	private void jedisDemo() {
		log.info("====== JEDIS DEMO ======");
		log.info(jedisPoolConfig.toString());
		try (Jedis jedis = jedisPool.getResource()) {
			coffeeService.findAllCoffee().forEach(coffee -> {
				jedis.hset(MENU, coffee.getName(), Long.toString(coffee.getPrice()));
			});
			log.info("Menu: {}", jedis.hgetAll(MENU));
			log.info("espresso - {}", jedis.hget(MENU, "espresso"));
		}
	}

	private void cacheDemo() {
		log.info("====== CACHE DEMO ======");
		List<Coffee> items = coffeeService.findAllCoffeeCacheable();
		log.info("Count: {}", items.size());
		for (int i = 0; i < 10; i++) {
			log.info("Reading from cache.");
		}
		coffeeService.reloadCoffee();
		log.info("Reading after refresh.");
		coffeeService
				.findAllCoffeeCacheable()
				.forEach(coffee -> log.info("Coffee {}", coffee.getName()));
	}

	private void redisTemplateDemo() {
		Optional<Coffee> mocha = coffeeService.findOneCoffeeWithRedisTemplate("mocha");
		log.info("Coffee {}", mocha);

		for (int i = 0; i < 5; i++) {
			mocha = coffeeService.findOneCoffeeWithRedisTemplate("mocha");
		}

		log.info("Value from Redis: {}", mocha);
	}
}
