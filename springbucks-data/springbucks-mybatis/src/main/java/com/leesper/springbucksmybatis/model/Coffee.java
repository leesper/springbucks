package com.leesper.springbucksmybatis.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString(callSuper = true)
public class Coffee extends BaseEntity {
    private String name;
    private Long price;
}
