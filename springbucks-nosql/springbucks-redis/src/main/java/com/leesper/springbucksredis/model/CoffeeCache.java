package com.leesper.springbucksredis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "springbucks-coffee", timeToLive = 60)
public class CoffeeCache {
    @Id
    private Long id;
    @Indexed
    private String name;
    private Long price;
}
