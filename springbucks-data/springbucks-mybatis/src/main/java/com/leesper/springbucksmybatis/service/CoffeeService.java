package com.leesper.springbucksmybatis.service;

import com.leesper.springbucksmybatis.mapper.CoffeeMapper;
import com.leesper.springbucksmybatis.model.Coffee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Mapper
public class CoffeeService {
    @Autowired
    private CoffeeMapper coffeeMapper;

    public List<Coffee> findAll() {
        return coffeeMapper.findAll();
    }

    public Optional<Coffee> findOneCoffee(String name) {
        return coffeeMapper.findOneCoffee(name);
    }
}
