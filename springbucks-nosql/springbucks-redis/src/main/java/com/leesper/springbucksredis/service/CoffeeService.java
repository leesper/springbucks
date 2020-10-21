package com.leesper.springbucksredis.service;

import com.leesper.springbucksredis.model.Coffee;
import com.leesper.springbucksredis.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;


@Service
@Slf4j
@EnableCaching
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;
    private final String CACHE = "coffee";

    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withMatcher(name, exact().ignoreCase());
        return coffeeRepository.findOne(Example.of(Coffee.builder().name(name).build(), matcher));
    }

    @Cacheable(CACHE)
    public List<Coffee> findAllCoffeeCacheable() {
        return coffeeRepository.findAll();
    }

    @CacheEvict(value = CACHE)
    public void reloadCoffee() {}

}
