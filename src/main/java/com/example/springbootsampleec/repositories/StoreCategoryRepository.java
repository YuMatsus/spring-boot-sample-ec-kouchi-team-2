package com.example.springbootsampleec.repositories;
 
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springbootsampleec.entities.StoreCategory;
 
@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
    // 料理IDに一致するレコードを取得
    List<StoreCategory> findByCategorizedStoresId(Long storeId);
    // 複数のIDに一致するレコードを全て取得
    Set<StoreCategory> findByIdIn(Set<Long> ids);
}