package com.leesper.springbucksredismongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coffee {
    private String id;
    private String name;
    private Long price;
}
