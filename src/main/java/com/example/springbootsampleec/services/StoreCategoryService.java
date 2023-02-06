package com.example.springbootsampleec.services;

public interface StoreCategoryService {
    // 登録
    void register(String name);
    // 更新
    void update(long id, String name);
    // 削除
    void delete(long id);
}