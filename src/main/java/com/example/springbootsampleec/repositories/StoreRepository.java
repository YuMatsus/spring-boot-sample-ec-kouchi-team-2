package com.example.springbootsampleec.repositories;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springbootsampleec.entities.Store;
 
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // ユーザーIDから取得
    List<Store> findByUserId(long id);
    // 料理IDから取得
    Store findByItemsId(long id);
    // 店舗カテゴリーIDから取得
    List<Store> findByStoreCategoriesId(long id);
    // 特定のロールのユーザーが管理する店舗を取得
    List<Store> findByUserRolesContaining(String roles);


}