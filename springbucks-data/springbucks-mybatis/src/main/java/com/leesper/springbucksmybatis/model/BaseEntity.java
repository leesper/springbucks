package com.leesper.springbucksmybatis.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class BaseEntity {
    private Long id;
    private Date createTime;
    private Date updateTime;
}
