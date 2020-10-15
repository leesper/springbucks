package com.leesper.springbucksjpa.repository;

import com.leesper.springbucksjpa.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
