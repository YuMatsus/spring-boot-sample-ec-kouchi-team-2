package com.example.springbootsampleec.controllers;
 
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springbootsampleec.entities.Item;
import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.services.ItemService;
import com.example.springbootsampleec.services.StoreService;
 
@RequestMapping("/owner")
@Controller
public class OwnerController {
    private final StoreService storeService;
    private final ItemService itemService;
        
    public OwnerController(StoreService storeService, ItemService itemService) {
        this.storeService = storeService;
        this.itemService = itemService;
    }
    
    // 店舗管理画面
    @GetMapping("/stores/")    
    public String manageStores(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<Store> stores = storeService.findByUserId(loginUser.getId());
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("stores", stores);
        model.addAttribute("title", "店舗管理");
        model.addAttribute("main", "owners/manage_stores::main");
        return "layout/logged_in";    
    }

    // 料理管理画面
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasStore(#storeId)")
    @GetMapping("/items/{storeId}")    
    public String manageItems(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("storeId") Long storeId,
        Model model
    ) {
        Store store = storeService.findById(storeId).orElseThrow();
        List<Item> items = itemService.findByStoreId(storeId);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("store", store);
        model.addAttribute("items", items);
        model.addAttribute("title", "料理管理");
        model.addAttribute("main", "owners/manage_items::main");
        return "layout/logged_in";    
    }

}