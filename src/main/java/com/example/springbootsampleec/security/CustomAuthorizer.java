package com.example.springbootsampleec.security;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.springbootsampleec.entities.Item;
import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.services.ItemService;
import com.example.springbootsampleec.services.StoreService;

@Component
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomAuthorizer {
    private final StoreService storeService;
    private final ItemService itemService;

        public CustomAuthorizer(StoreService storeService, ItemService itemService) {
        this.storeService = storeService;
        this.itemService = itemService;
    }

    // 店舗IDから認証
    public boolean hasStore(Long storeId) {
        var simpleLoginUser = (SimpleLoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = simpleLoginUser.getUser().getId();
        Store store = storeService.findById(storeId).orElseThrow();
        Long userId = store.getUser().getId();
        if(loginUserId == userId){
            return true;
        }
        return false;
    }

    // 料理IDから認証
    public boolean hasItem(Long itemId) {
        var simpleLoginUser = (SimpleLoginUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = simpleLoginUser.getUser().getId();
        Item item = itemService.findById(itemId).orElseThrow();
        Long userId = item.getStore().getUser().getId();
        if(loginUserId == userId){
            return true;
        }
        return false;
    }
}
