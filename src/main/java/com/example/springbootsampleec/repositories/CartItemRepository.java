package com.example.springbootsampleec.repositories;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springbootsampleec.entities.CartItem;
 
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // ユーザーIDとアイテムIDに一致するレコードの存在確認
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
    // ユーザーIDとアイテムIDに一致するレコードを取得
    CartItem findByUserIdAndItemId(Long userId, Long itemId);
    // ユーザーIDに一致するレコードを取得
    List<CartItem> findByUserId(Long userId);
    // ユーザーIDと一致し、かつアイテムの店舗IDに一致しないレコードを取得
    List<CartItem> findByUserIdAndItemStoreIdNot(Long userId, Long itemId);
    // ユーザーIDに一致するレコードを削除
    List<CartItem> deleteByUserId(Long userId);
    // ユーザーIDとアイテムIDに一致するレコードを削除
    Void deleteByUserIdAndItemId(Long userId, Long itemId);
}