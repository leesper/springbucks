package com.leesper.springbucksjpa.service;

import com.leesper.springbucksjpa.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;
}
