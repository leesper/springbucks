package com.leesper.springbucksmybatis.model;

import lombok.*;

import java.util.List;

@Builder
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeOrder extends BaseEntity {
    private String customer;
    private List<Coffee> items;
    private OrderState state;
}
