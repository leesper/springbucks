package com.leesper.springbucksredis;

import com.leesper.springbucksredis.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
@EnableTransactionManagement
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
		jedisDemo();
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
}
