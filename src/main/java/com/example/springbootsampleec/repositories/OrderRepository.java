package com.example.springbootsampleec.repositories;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springbootsampleec.entities.Order;
 
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(long id);

}