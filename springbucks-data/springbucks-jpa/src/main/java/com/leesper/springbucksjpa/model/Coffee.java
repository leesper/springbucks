package com.leesper.springbucksjpa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity(name = "t_coffee")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coffee extends BaseEntity {
    private String name;
    private Long price;
}
