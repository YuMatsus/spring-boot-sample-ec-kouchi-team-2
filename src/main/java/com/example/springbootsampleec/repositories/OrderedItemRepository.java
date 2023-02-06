package com.example.springbootsampleec.repositories;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springbootsampleec.entities.OrderedItem;
 
@Repository
public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {
    // 注文IDから注文されたアイテム一覧を取得
    List<OrderedItem> findByOrderId(long id);
}