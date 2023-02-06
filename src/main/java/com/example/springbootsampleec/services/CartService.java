package com.example.springbootsampleec.services;

import java.util.List;

import com.example.springbootsampleec.entities.CartItem;
import com.example.springbootsampleec.entities.Order;
import com.example.springbootsampleec.entities.User;

public interface CartService {
    // カートに追加
    void add(Long userId, Long itemId);
    // カートから削除
    void delete(Long userId, Long itemId);
    // 数量の変更
    void updateQuantity(Long id, int quantity);
    // 注文処理
    void order(User user, List<CartItem> cartItems);
    // カートを空にする
    void emptyCart(User user);
    // 注文履歴からカートに追加
    void addFromOrderHistory(Order order);
}