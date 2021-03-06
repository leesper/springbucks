package com.leesper.springbucksjpa;

import com.leesper.springbucksjpa.model.Coffee;
import com.leesper.springbucksjpa.model.CoffeeOrder;
import com.leesper.springbucksjpa.model.OrderState;
import com.leesper.springbucksjpa.service.CoffeeOrderService;
import com.leesper.springbucksjpa.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@SpringBootApplication
@Slf4j
@EnableJpaRepositories
@EnableTransactionManagement
public class SpringbucksJpaApplication implements ApplicationRunner {
	@Autowired
	private CoffeeService coffeeService;

	@Autowired
	private CoffeeOrderService coffeeOrderService;

	public static void main(String[] args) {
		SpringApplication.run(SpringbucksJpaApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("All coffee: {}", coffeeService.findAll());

		Optional<Coffee> latte = coffeeService.findOneCoffee("latte");
		if (latte.isPresent()) {
			CoffeeOrder order = coffeeOrderService.createOrder("Li Lei", latte.get());
			log.info("Update to PAID: {}", coffeeOrderService.updateState(order, OrderState.PAID));
			log.info("Update to INIT: {}", coffeeOrderService.updateState(order, OrderState.INIT));
		}
	}
}
