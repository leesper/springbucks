package com.leesper.springbucksmybatis.service;

import com.leesper.springbucksmybatis.mapper.CoffeeOrderMapper;
import com.leesper.springbucksmybatis.model.Coffee;
import com.leesper.springbucksmybatis.model.CoffeeOrder;
import com.leesper.springbucksmybatis.model.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderMapper coffeeOrderMapper;

    public CoffeeOrder createOrder(String customer, Coffee... coffee) {
        return coffeeOrderMapper.createOrder(customer, coffee);
    }
    public boolean updateState(CoffeeOrder order, OrderState state) {
        return coffeeOrderMapper.updateState(order, state);
    }
}
