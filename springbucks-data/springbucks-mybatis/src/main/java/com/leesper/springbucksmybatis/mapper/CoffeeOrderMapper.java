package com.leesper.springbucksmybatis.mapper;

import com.leesper.springbucksmybatis.model.Coffee;
import com.leesper.springbucksmybatis.model.CoffeeOrder;
import com.leesper.springbucksmybatis.model.OrderState;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CoffeeOrderMapper {
    public void addOrder(CoffeeOrder order);
    public void addOrderCoffee(@Param("idBag") List<Map<String, Long>> idBag);
    public void update(CoffeeOrder order);
}
