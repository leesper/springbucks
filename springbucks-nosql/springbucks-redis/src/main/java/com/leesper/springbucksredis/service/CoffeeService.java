package com.leesper.springbucksredis.service;

import com.leesper.springbucksredis.model.Coffee;
import com.leesper.springbucksredis.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;


@Service
@Slf4j
@CacheConfig(cacheNames = "coffee")
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    private final String CACHE = "springbucks-coffee";

    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withMatcher(name, exact().ignoreCase());
        return coffeeRepository.findOne(Example.of(Coffee.builder().name(name).build(), matcher));
    }

    @Cacheable
    public List<Coffee> findAllCoffeeCacheable() {
        return coffeeRepository.findAll();
    }

    @CacheEvict
    public void reloadCoffee() {}

    public Optional<Coffee> findOneCoffeeWithRedisTemplate(String name) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (redisTemplate.hasKey(CACHE) && hashOperations.hasKey(CACHE, name)) {
            Coffee coffee = (Coffee) hashOperations.get(CACHE, name);
            log.info("Get coffee {} from Redis.", coffee.getName());
            return Optional.of(coffee);
        }

        Optional<Coffee> result = findOneCoffee(name);
        if (result.isPresent()) {
            hashOperations.put(CACHE, name, result.get());
            redisTemplate.expire(CACHE, 1L, TimeUnit.MINUTES);
        }

        return result;
    }

}
