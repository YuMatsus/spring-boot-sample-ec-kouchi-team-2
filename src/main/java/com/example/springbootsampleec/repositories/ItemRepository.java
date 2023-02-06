package com.example.springbootsampleec.repositories;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.example.springbootsampleec.entities.Item;

import java.util.List;
 
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 店舗IDからアイテム一覧を取得
    List<Item> findByStoreId(long id);
    // ユーザーIDからカートのアイテム一覧を取得
    List<Item> findByCartItemsUserId(long id);
    // 料理カテゴリーIDから取得
    List<Item> findByItemCategoriesId(long id);
    // 料理カテゴリーIDと店舗IDから取得
    List<Item> findByItemCategoriesIdAndStoreId(long categoryId, long storeId);
    // 栄養素IDから取得
    List<Item> findByNutrientsId(long id);
    // 栄養素IDと店舗IDから取得
    List<Item> findByNutrientsIdAndStoreId(long nutrientId, long storeId);
    // ユーザーロールから取得
    List<Item> findByStoreUserRolesContaining(String roles);
}