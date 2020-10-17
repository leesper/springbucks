package com.leesper.springbucksmybatis.model;

import lombok.*;

@Data
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Coffee extends BaseEntity {
    private String name;
    private Long price;
}
