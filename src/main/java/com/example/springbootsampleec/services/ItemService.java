package com.example.springbootsampleec.services;
 
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.example.springbootsampleec.entities.Item;
import com.example.springbootsampleec.entities.Store;
 
public interface ItemService {
    // 料理一覧の取得
    List<Item> findAll();
    // 指定した数の料理をランダムに取得
    List<Item> getRandomItems(Integer quantity);
    // ID を指定して料理を取得
    Optional<Item> findById(long id);
    // 店舗IDから料理一覧を取得
    List<Item> findByStoreId(long id);
    // 料理情報を更新
    void updateItem(long id, String name, int price,int calorie, Set<Long> categoryIds, Set<Long> nutrientIds, String description);
    // 削除
    void delete(long id);
    // 料理の登録
    void register(Store store, String name, int price,int calorie, Set<Long> categoryIds, Set<Long> nutrientIds, String description, MultipartFile image);
    
    // 料理画像の更新
    Item updateImage(Long id, MultipartFile image);
}