package com.leesper.springbucksjpa.model;

import lombok.Data;
import javax.persistence.Entity;

@Entity(name = "t_coffee")
@Data
public class Coffee extends BaseEntity {
    private String name;
    private Long price;
}
