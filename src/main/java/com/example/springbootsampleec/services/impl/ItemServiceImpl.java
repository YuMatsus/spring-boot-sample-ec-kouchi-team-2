package com.example.springbootsampleec.services.impl;
 
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.springbootsampleec.entities.Item;
import com.example.springbootsampleec.entities.ItemCategory;
import com.example.springbootsampleec.entities.Nutrient;
import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.repositories.ItemCategoryRepository;
import com.example.springbootsampleec.repositories.ItemRepository;
import com.example.springbootsampleec.repositories.NutrientRepository;
import com.example.springbootsampleec.services.FileService;
import com.example.springbootsampleec.services.ItemService;

 
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private NutrientRepository nutrientRepository;
    @Autowired
    private FileService fileService;

    // 料理一覧の取得
    @Transactional(readOnly = true)
    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
    
    // 料理テーブルから指定した数だけ取得
    @Transactional(readOnly = true)
    @Override
    public List<Item> getRandomItems(Integer quantity) {
        List<Item> allItems = itemRepository.findAll();
		Collections.shuffle(allItems);
		int itemCount = allItems.size();
		List<Item> items;
		if(itemCount < quantity) {
			items = allItems.subList(0, itemCount);
		} else {
			items = allItems.subList(0, quantity);
		}
        return items;
    }

    // ID を指定して料理を取得
    @Transactional(readOnly = true)
    @Override
    public Optional<Item> findById(long id) {
        return itemRepository.findById(id);
    }

    // 店舗IDから料理一覧を取得
    @Transactional(readOnly = true)
    @Override
    public List<Item> findByStoreId(long id) {
        return itemRepository.findByStoreId(id);
    }
    
    // 料理情報を更新
    @Transactional
    @Override
    public void updateItem(long id, String name, int price,int calorie, Set<Long> categoryIds, Set<Long> nutrientIds, String description) {
        Item item =  findById(id).orElseThrow();
        item.setName(name);
        item.setPrice(price);
        item.setCalorie(calorie);
        item.setItemCategories(itemCategoryRepository.findByIdIn(categoryIds));
        item.setNutrients(nutrientRepository.findByIdIn(nutrientIds));
        item.setDescription(description);
        itemRepository.saveAndFlush(item);
    }
    
    // 削除
    @Transactional
    @Override
    public void delete(long id) {
        Item item =  findById(id).orElseThrow();
        itemRepository.delete(item);
    }

    // 料理の登録
    @Transactional
    @Override
    public void register(Store store, String name, int price, int calorie, Set<Long> categoryIds, Set<Long> nutrientIds, String description, MultipartFile image) {
        String filename = fileService.uploadImage(image);
        Set<ItemCategory> itemCategories = itemCategoryRepository.findByIdIn(categoryIds);
        Set<Nutrient> nutrients = nutrientRepository.findByIdIn(nutrientIds);
        Item item = new Item(null, store, null, null, name, price, calorie, itemCategories, nutrients, description, filename, 0, null, null);
        itemRepository.saveAndFlush(item);
    }

    // 店舗画像の更新
	@Transactional
    @Override
    public Item updateImage(Long id, MultipartFile image) {
		String filename = fileService.uploadImage(image);
		Item item = itemRepository.findById(id).orElseThrow();
		item.setImage(filename);
		itemRepository.saveAndFlush(item);	
		return item;
	}

}