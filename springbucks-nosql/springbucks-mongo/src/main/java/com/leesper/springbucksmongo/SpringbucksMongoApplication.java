package com.leesper.springbucksmongo;

import com.leesper.springbucksmongo.model.Coffee;
import com.leesper.springbucksmongo.repository.CoffeeRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@Slf4j
@EnableMongoRepositories
public class SpringbucksMongoApplication implements ApplicationRunner {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CoffeeRepository coffeeRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringbucksMongoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		mongoTemplateDemo();
		mongoRepositoryDemo();
	}

	private void mongoTemplateDemo() {
		log.info("====== mongoTemplate demo ======");
		Coffee espresso = Coffee.builder()
				.name("espresso")
				.price(2000L)
				.createTime(new Date())
				.updateTime(new Date())
				.build();
		Coffee saved = mongoTemplate.save(espresso);
		log.info("Coffee {}", saved);

		Query query = new Query(Criteria.where("name").is("espresso"));
		List<Coffee> items = mongoTemplate.find(query, Coffee.class);
		log.info("Find {} Coffee", items.size());
		items.forEach(c -> log.info("Coffee {}", c));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		UpdateResult result = mongoTemplate.updateFirst(query,
				new Update()
						.set("price", 3000L)
						.currentDate("updateTIme"),
				Coffee.class);
		log.info("Update Result: {}", result.getModifiedCount());

		Coffee c = mongoTemplate.findById(saved.getId(), Coffee.class);
		log.info("Update Result: {}", c);

		mongoTemplate.remove(c);
	}

	private void mongoRepositoryDemo() {
		log.info("====== mongoRepository demo ======");
		Coffee espresso = Coffee.builder()
				.name("espresso")
				.price(2000L)
				.createTime(new Date())
				.updateTime(new Date())
				.build();

		Coffee latte = Coffee.builder()
				.name("latte")
				.price(3000L)
				.createTime(new Date())
				.updateTime(new Date())
				.build();

		coffeeRepository.insert(Arrays.asList(espresso, latte));
		coffeeRepository.findAll(Sort.by("name")).forEach(c -> log.info("Saved Coffee {}", c));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		latte.setPrice(3500L);
		latte.setUpdateTime(new Date());
		coffeeRepository.save(latte);
		coffeeRepository.findByName("latte").forEach(c -> log.info("Coffee {}", c));

		coffeeRepository.deleteAll();
	}
}
