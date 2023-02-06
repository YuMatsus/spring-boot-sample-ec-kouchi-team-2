package com.example.springbootsampleec.services.impl;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.entities.StoreCategory;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.repositories.StoreCategoryRepository;
import com.example.springbootsampleec.repositories.StoreRepository;
import com.example.springbootsampleec.services.FileService;
import com.example.springbootsampleec.services.GeoApiService;
import com.example.springbootsampleec.services.StoreService;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;

 
@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    @Autowired
	private FileService fileService;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    @Autowired
    private GeoApiService geoApiService;
 
    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }
 
    @Transactional(readOnly = true)
    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Store> findByUserId(long id) {
        return storeRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Store> findById(long id) {
        return storeRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Store findByItemsId(long id) {
        return storeRepository.findByItemsId(id);
    }

    // 店舗情報の更新処理
    @Transactional
    @Override
    public void update(long id, String name, String address, String description, Set<Long> categoryIds) {
        Store store =  findById(id).orElseThrow();
        LatLng geocode = geoApiService.getLatLng(address);
        double lat = 0;
        double lng = 0;
        if(geocode != null){
            lat = geocode.lat;
            lng = geocode.lng;
        }
        store.setName(name);
        store.setAddress(address);
        store.setDescription(description);
        store.setStoreCategories(storeCategoryRepository.findByIdIn(categoryIds));
        store.setLat(lat);
        store.setLng(lng);
        storeRepository.saveAndFlush(store);
    }
    
    // 削除
    @Transactional
    @Override
    public void delete(long id) {
        Store store =  findById(id).orElseThrow();
        storeRepository.delete(store);
    }

    // 店舗の登録処理
    @Transactional
    @Override
    public void register(User user, String name, String address, String description, MultipartFile image, Set<Long> categoryIds) {
        String filename = fileService.uploadImage(image);
        LatLng geocode = geoApiService.getLatLng(address);
        double lat = 0;
        double lng = 0;
        if(geocode != null){
            lat = geocode.lat;
            lng = geocode.lng;
        }
        Set<StoreCategory> storeCategories = storeCategoryRepository.findByIdIn(categoryIds);
        Store store = new Store(null, user, null, name, address, lat, lng, description, filename, storeCategories, null, null);
        storeRepository.saveAndFlush(store);
    }

    // 店舗画像の更新
	@Transactional
    @Override
    public Store updateImage(Long id, MultipartFile image) {
		String filename = fileService.uploadImage(image);
		Store store = storeRepository.findById(id).orElseThrow();
		store.setImage(filename);
		storeRepository.saveAndFlush(store);	
		return store;
	}

    // 近隣の店舗を取得
    @Transactional
    @Override
    public List<Store> getNearbyStores(User user) {
        List<Store> stores = storeRepository.findByUserRolesContaining("OWNER");

        // ユーザーの座標が未登録( = 0) の場合は全ての店舗を返す
        if(user.getLat() == 0 || user.getLng() == 0) return stores;

        // ユーザーの座標
        LatLng[] userLatLngs = new LatLng[1];
        userLatLngs[0] = new LatLng(user.getLat(), user.getLng());

        // 店舗の座標
        LatLng[] storeLatLngs = new LatLng[stores.size()];
        int i = -1;
        for (Store store : stores) {
            i++;
            storeLatLngs[i] = new LatLng(store.getLat(), store.getLng());
        }

        // 経路情報の取得
        DistanceMatrix distanceMatrix = geoApiService.getDistanceMatrix(userLatLngs, storeLatLngs);
        // 取得できない場合は全ての店舗を返す
        if(distanceMatrix == null) return stores;

        // 店舗からユーザーまでの所要時間が1800秒を超える場合はリストから除外
        List<Store> nearbyStores = new ArrayList<Store>();
        i = -1;
        for (Store store : stores) {
            i++;
            if(store.getLat() == 0 || store.getLng() == 0) {
                nearbyStores.add(store);
                continue;
            } 
            if(distanceMatrix.rows[0].elements[i].duration.inSeconds < 1800) {
                nearbyStores.add(store);
                continue;
            }
        }

        return nearbyStores;
    }
}