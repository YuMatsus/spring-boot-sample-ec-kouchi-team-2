package com.example.springbootsampleec.services.impl;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootsampleec.entities.StoreCategory;
import com.example.springbootsampleec.repositories.StoreCategoryRepository;
import com.example.springbootsampleec.services.StoreCategoryService;

 
@Service
public class StoreCategoryServiceImpl implements StoreCategoryService {
    @Autowired
    private StoreCategoryRepository storeCategoryRepository;


    // 登録
    @Transactional
    @Override
    public void register(String name) {
        StoreCategory storeCategory = new StoreCategory(null, null, name, null, null);
        storeCategoryRepository.saveAndFlush(storeCategory);
    }

    // 更新
    @Transactional
    @Override
    public void update(long id, String name) {
        StoreCategory storeCategory =  storeCategoryRepository.findById(id).orElseThrow();
        storeCategory.setName(name);
        storeCategoryRepository.saveAndFlush(storeCategory);
    }
    
    // 削除
    @Transactional
    @Override
    public void delete(long id) {
        StoreCategory storeCategory =  storeCategoryRepository.findById(id).orElseThrow();
        storeCategoryRepository.delete(storeCategory);
    }
}