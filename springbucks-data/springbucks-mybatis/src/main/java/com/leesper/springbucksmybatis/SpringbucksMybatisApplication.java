package com.leesper.springbucksmybatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SpringbucksMybatisApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbucksMybatisApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("hello spring mybatis");
	}
}
