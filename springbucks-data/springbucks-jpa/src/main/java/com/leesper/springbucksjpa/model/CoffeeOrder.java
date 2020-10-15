package com.leesper.springbucksjpa.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity(name = "t_order")
@Data
public class CoffeeOrder {
    @ManyToMany
    @JoinTable(name = "t_order_coffee")
    private List<Coffee> coffees;
    @Enumerated
    private OrderState orderState;
}
