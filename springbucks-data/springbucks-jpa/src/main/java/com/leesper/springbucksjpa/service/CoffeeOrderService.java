package com.leesper.springbucksjpa.service;

import com.leesper.springbucksjpa.repository.CoffeeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;
}
