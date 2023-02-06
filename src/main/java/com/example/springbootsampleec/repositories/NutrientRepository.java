package com.example.springbootsampleec.repositories;
 
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springbootsampleec.entities.Nutrient;
 
@Repository
public interface NutrientRepository extends JpaRepository<Nutrient, Long> {
    // 料理IDに一致するレコードを取得
    List<Nutrient> findByItemsId(Long itemId);
    // 複数のIDに一致するレコードを全て取得
    Set<Nutrient> findByIdIn(Set<Long> ids);
}