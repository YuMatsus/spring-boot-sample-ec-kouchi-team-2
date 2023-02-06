package com.example.springbootsampleec.repositories;
 
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springbootsampleec.entities.ItemCategory;
 
@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    // 料理IDに一致するレコードを取得
    List<ItemCategory> findByCategorizedItemsId(Long itemId);
    // 複数のIDに一致するレコードを全て取得
    Set<ItemCategory> findByIdIn(Set<Long> ids);
}