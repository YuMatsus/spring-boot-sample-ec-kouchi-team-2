package com.example.springbootsampleec.services;
 
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.entities.User;
 
public interface StoreService {
    // 店舗一覧の取得
    List<Store> findAll();
    // ユーザーIDから店舗一覧を取得
    List<Store> findByUserId(long id);
    // ID を指定して店舗を取得
    Optional<Store> findById(long id);
    // 料理IDから店舗を取得
    Store findByItemsId(long id);
    // 店舗情報を更新
    void update(long id, String name, String address, String description, Set<Long> categoryIds);
    // 削除
    void delete(long id);
    // 店舗の登録
    void register(User user, String name, String address, String description, MultipartFile image, Set<Long> categoryIds);
    // 店舗画像の更新
    Store updateImage(Long id, MultipartFile image);
    // 近隣の店舗を取得
    List<Store> getNearbyStores(User user);
}