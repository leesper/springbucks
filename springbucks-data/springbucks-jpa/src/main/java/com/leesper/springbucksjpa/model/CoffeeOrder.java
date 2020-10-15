package com.leesper.springbucksjpa.model;

import com.sun.xml.internal.rngom.parse.host.Base;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "t_order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class CoffeeOrder extends BaseEntity {
    private String customer;

    @ManyToMany
    @JoinTable(name = "t_order_coffee")
    @OrderBy("id")
    private List<Coffee> coffees;

    @Enumerated
    @Column(nullable = false)
    private OrderState orderState;
}
