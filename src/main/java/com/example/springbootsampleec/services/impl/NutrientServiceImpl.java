package com.example.springbootsampleec.services.impl;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootsampleec.entities.Nutrient;
import com.example.springbootsampleec.repositories.NutrientRepository;
import com.example.springbootsampleec.services.NutrientService;

 
@Service
public class NutrientServiceImpl implements NutrientService {
    @Autowired
    private NutrientRepository nutrientRepository;


    // 登録
    @Transactional
    @Override
    public void register(String name) {
        Nutrient nutrient = new Nutrient(null, null, name, null, null);
        nutrientRepository.saveAndFlush(nutrient);
    }

    // 更新
    @Transactional
    @Override
    public void update(long id, String name) {
        Nutrient nutrient =  nutrientRepository.findById(id).orElseThrow();
        nutrient.setName(name);
        nutrientRepository.saveAndFlush(nutrient);
    }
    
    // 削除
    @Transactional
    @Override
    public void delete(long id) {
        Nutrient nutrient =  nutrientRepository.findById(id).orElseThrow();
        nutrientRepository.delete(nutrient);
    }
}