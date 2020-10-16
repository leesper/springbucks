package com.leesper.springbucksjpa.service;

import com.leesper.springbucksjpa.model.Coffee;
import com.leesper.springbucksjpa.model.CoffeeOrder;
import com.leesper.springbucksjpa.model.OrderState;
import com.leesper.springbucksjpa.repository.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    public List<Coffee> findAll() {
        return coffeeRepository.findAll();
    }

    public Optional<Coffee> findOneCoffee(String name) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase();
        return coffeeRepository.findOne(Example.of(Coffee.builder().name(name).build(), matcher));
    }


}
