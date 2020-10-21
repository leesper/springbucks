package com.leesper.springbucksredis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
@EnableTransactionManagement
public class SpringbucksRedisApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbucksRedisApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		jedisDemo();
	}

	private void jedisDemo() {
		log.info("====== JEDIS DEMO ======");
	}
}
