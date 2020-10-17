package com.leesper.springbucksmybatis;

import com.leesper.springbucksmybatis.model.Coffee;
import com.leesper.springbucksmybatis.model.CoffeeOrder;
import com.leesper.springbucksmybatis.model.OrderState;
import com.leesper.springbucksmybatis.service.CoffeeOrderService;
import com.leesper.springbucksmybatis.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@Slf4j
@MapperScan("com.leesper.springbucksmybatis.mapper")
public class SpringbucksMybatisApplication implements ApplicationRunner {
	@Autowired
	private CoffeeService coffeeService;

	@Autowired
	private CoffeeOrderService coffeeOrderService;

	public static void main(String[] args) {
		SpringApplication.run(SpringbucksMybatisApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("hello spring mybatis");
		List<Coffee> coffees = coffeeService.findAll();
		log.info("All coffee: {}", coffees);

		Optional<Coffee> latte = coffeeService.findOneCoffee("latte");
		if (latte.isPresent()) {
			log.info("Present: {}", latte.get());
//			CoffeeOrder order = coffeeOrderService.createOrder("Li Lei", latte.get());
//			log.info("New order {}", order);

//			log.info("Update to PAID: {}", coffeeOrderService.updateState(order, OrderState.PAID));
//			log.info("Update to INIT: {}", coffeeOrderService.updateState(order, OrderState.INIT));
		}
	}
}
