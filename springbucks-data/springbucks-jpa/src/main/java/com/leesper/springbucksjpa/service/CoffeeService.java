package com.leesper.springbucksjpa.service;

import com.leesper.springbucksjpa.model.Coffee;
import com.leesper.springbucksjpa.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    public List<Coffee> findAll() {
        return coffeeRepository.findAll();
    }
}
