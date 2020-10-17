package com.leesper.springbucksmybatis.service;

import com.leesper.springbucksmybatis.mapper.CoffeeOrderMapper;
import com.leesper.springbucksmybatis.model.Coffee;
import com.leesper.springbucksmybatis.model.CoffeeOrder;
import com.leesper.springbucksmybatis.model.OrderState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderMapper coffeeOrderMapper;

    public CoffeeOrder createOrder(String customer, Coffee... coffee) {
        CoffeeOrder order = CoffeeOrder.builder()
                .customer(customer)
                .items(new ArrayList<>(Arrays.asList(coffee)))
                .state(OrderState.INIT)
                .build();
        coffeeOrderMapper.addOrder(order);
        log.info("idBag: {}", makeIdBag(order, coffee));
        coffeeOrderMapper.addOrderCoffee(makeIdBag(order, coffee));
        return order;
    }

    public boolean updateState(CoffeeOrder order, OrderState state) {
        if (state.compareTo(order.getState()) < 0) {
            log.info("Error state order {}, {}", order.getState(), state);
            return false;
        }
        order.setState(state);
        coffeeOrderMapper.update(order);
        return true;
    }

    private List<Map<String, Long>> makeIdBag(CoffeeOrder order, Coffee... coffee) {
        List<Map<String, Long>> idBag = new ArrayList<>();
        for (Coffee c : coffee) {
            Map<String, Long> bag = new HashMap<>();
            bag.put("order_id", order.getId());
            bag.put("coffee_id", c.getId());
            idBag.add(bag);
        }
        return idBag;
    }
}
