package com.leesper.springbucksmybatis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    private Long id;
    private Date createTime;
    private Date updateTime;
}
