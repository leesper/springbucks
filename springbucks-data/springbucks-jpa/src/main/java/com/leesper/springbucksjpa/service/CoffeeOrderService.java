package com.leesper.springbucksjpa.service;

import com.leesper.springbucksjpa.model.Coffee;
import com.leesper.springbucksjpa.model.CoffeeOrder;
import com.leesper.springbucksjpa.model.OrderState;
import com.leesper.springbucksjpa.repository.CoffeeOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@Slf4j
@Transactional
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public CoffeeOrder createOrder(String customer, Coffee... coffee) {
        CoffeeOrder order = CoffeeOrder.builder()
                .customer(customer)
                .items(new ArrayList<>(Arrays.asList(coffee)))
                .state(OrderState.INIT)
                .build();
        CoffeeOrder saved = coffeeOrderRepository.save(order);
        return saved;
    }

    public boolean updateState(CoffeeOrder order, OrderState state) {
        if (state.compareTo(order.getState()) < 0) {
            log.info("Error, state order {}, {}", order.getState(), state);
            return false;
        }
        order.setState(state);
        coffeeOrderRepository.save(order);
        return true;
    }
}
