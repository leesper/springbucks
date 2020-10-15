package com.leesper.springbucksjpa.service;

import com.leesper.springbucksjpa.repository.CoffeeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;
}
