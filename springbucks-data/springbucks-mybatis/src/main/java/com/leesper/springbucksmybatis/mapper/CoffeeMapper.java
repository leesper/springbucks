package com.leesper.springbucksmybatis.mapper;

import com.leesper.springbucksmybatis.model.Coffee;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface CoffeeMapper {
    public List<Coffee> findAll();

    public Optional<Coffee> findOneCoffee(@Param("name") String name);
}
