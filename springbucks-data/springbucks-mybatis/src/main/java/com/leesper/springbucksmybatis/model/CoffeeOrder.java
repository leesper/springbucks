package com.leesper.springbucksmybatis.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Builder
@Data
@ToString(callSuper = true)
public class CoffeeOrder {
    private String customer;
    private List<Coffee> items;
    private OrderState state;
}
