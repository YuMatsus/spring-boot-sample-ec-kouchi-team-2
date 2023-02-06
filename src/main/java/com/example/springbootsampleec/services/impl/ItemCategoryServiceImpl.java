package com.example.springbootsampleec.services.impl;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootsampleec.entities.ItemCategory;
import com.example.springbootsampleec.repositories.ItemCategoryRepository;
import com.example.springbootsampleec.services.ItemCategoryService;

 
@Service
public class ItemCategoryServiceImpl implements ItemCategoryService {
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;


    // 登録
    @Transactional
    @Override
    public void register(String name) {
        ItemCategory itemCategory = new ItemCategory(null, null, name, null, null);
        itemCategoryRepository.saveAndFlush(itemCategory);
    }

    // 更新
    @Transactional
    @Override
    public void update(long id, String name) {
        ItemCategory itemCategory =  itemCategoryRepository.findById(id).orElseThrow();
        itemCategory.setName(name);
        itemCategoryRepository.saveAndFlush(itemCategory);
    }
    
    // 削除
    @Transactional
    @Override
    public void delete(long id) {
        ItemCategory itemCategory =  itemCategoryRepository.findById(id).orElseThrow();
        itemCategoryRepository.delete(itemCategory);
    }
}