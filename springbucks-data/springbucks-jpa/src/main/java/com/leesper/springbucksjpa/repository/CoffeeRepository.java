package com.leesper.springbucksjpa.repository;

import com.leesper.springbucksjpa.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
