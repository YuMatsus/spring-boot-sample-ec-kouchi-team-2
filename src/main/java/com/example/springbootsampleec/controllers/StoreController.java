package com.example.springbootsampleec.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springbootsampleec.entities.CartItem;
import com.example.springbootsampleec.entities.Item;
import com.example.springbootsampleec.entities.ItemCategory;
import com.example.springbootsampleec.entities.Nutrient;
import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.entities.StoreCategory;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.forms.EditImageForm;
import com.example.springbootsampleec.forms.RegisterStoreForm;
import com.example.springbootsampleec.forms.StoreEditForm;
import com.example.springbootsampleec.repositories.CartItemRepository;
import com.example.springbootsampleec.repositories.ItemCategoryRepository;
import com.example.springbootsampleec.repositories.ItemRepository;
import com.example.springbootsampleec.repositories.NutrientRepository;
import com.example.springbootsampleec.repositories.StoreCategoryRepository;
import com.example.springbootsampleec.repositories.StoreRepository;
import com.example.springbootsampleec.services.ItemService;
import com.example.springbootsampleec.services.StoreService;

@RequestMapping("/stores")
@Controller
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreCategoryRepository storeCategoryRepository;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private NutrientRepository nutrientRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

	//店舗一覧
	@GetMapping("/")
	public String index(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @RequestParam(value = "id", required = false) Long userId,
        Model model
    ) {
        // トップに表示する料理情報の取得
        List<Item> items = itemService.getRandomItems(12);		
		List<ItemCategory> itemCategories = itemCategoryRepository.findAll();
        List<Nutrient> nutrients = nutrientRepository.findAll();

        // 店舗の取得
        List<Store> stores = storeService.getNearbyStores(loginUser);
        List<StoreCategory> storeCategories = storeCategoryRepository.findAll();

        model.addAttribute("items", items);
		model.addAttribute("itemCategories", itemCategories);
        model.addAttribute("nutrients", nutrients);
        model.addAttribute("stores", stores);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("storeCategories", storeCategories);
        model.addAttribute("main", "stores/index::main");
        return "layout/logged_in";
	}

    //店舗一覧 フィルター処理
	@PostMapping("/")
	public String indexFiltered(
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam(value = "name", required = false) String name,
        Model model
    ) {
        List<Store> stores = new ArrayList<Store>();
        if (id == 0) {
            stores = storeRepository.findAll();
        } else {
            if(name.equals("category")) {
                stores = storeRepository.findByStoreCategoriesId(id);
            } else {
                stores = storeRepository.findAll();
            }            
        }
        model.addAttribute("stores", stores);
        return "stores/index::storeView";
	}

	// 店舗の登録
    @GetMapping("/register")    
    public String registerStore(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("registerStoreForm") RegisterStoreForm registerStoreForm,
        Model model
        ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("storeCategories", storeCategoryRepository.findAll());
        model.addAttribute("title", "店舗登録");
        model.addAttribute("main", "stores/register::main");
        return "layout/logged_in";
    }

	// 店舗の登録処理 
    @PostMapping("/register")
    public String registerStoreProcess(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @Valid RegisterStoreForm registerStoreForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model){
        if(bindingResult.hasErrors()){
            return registerStore(loginUser, registerStoreForm, model);
        }        
        storeService.register(
            loginUser,
            registerStoreForm.getName(),
            registerStoreForm.getAddress(),
            registerStoreForm.getDescription(),
            registerStoreForm.getImage(),
            registerStoreForm.getStoreCategoryIds()
            );
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "店舗の登録が完了しました");
        return "redirect:/owner/stores/";
    }

	// 店舗情報詳細
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasStore(#id)")
    @GetMapping("/detail/{id}")    
    public String detail(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Integer id,
        Model model
    ) {
        Store store = storeService.findById(id).orElseThrow();
        model.addAttribute("store", store);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "店舗詳細");
        model.addAttribute("main", "stores/detail::main");
        return "layout/logged_in";    
    }

	// 店舗の編集
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasStore(#id)")
    @GetMapping("/edit/{id}")    
    public String edit(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("storeEditForm") StoreEditForm storeEditForm,
        @PathVariable("id")  Integer id,
        Model model
    ) {
        Store store = storeService.findById(id).orElseThrow();
        // 既に紐づけられている店舗カテゴリーIDを取得
        List<StoreCategory> storeCategories = storeCategoryRepository.findByCategorizedStoresId(store.getId());
        Set<Long> storeCategoryIds = new HashSet<>();
        for (StoreCategory storeCategory : storeCategories) storeCategoryIds.add(storeCategory.getId());
        storeEditForm.setName(store.getName());
        storeEditForm.setAddress(store.getAddress());
        storeEditForm.setDescription(store.getDescription());
        storeEditForm.setStoreCategoryIds(storeCategoryIds);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("store", store);
        model.addAttribute("storeCategories", storeCategoryRepository.findAll());
        model.addAttribute("title", "店舗情報を編集");
        model.addAttribute("main", "stores/edit::main");
        return "layout/logged_in";    
    }

	// 店舗の編集処理
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasStore(#id)")
    @PostMapping("/update/{id}")    
    public String update(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Integer id,
        @Valid StoreEditForm storeEditForm,
        RedirectAttributes redirectAttributes,
        BindingResult bindingResult,
        Model model) {
        if(bindingResult.hasErrors()){
            return edit(loginUser, storeEditForm, id, model);
        }
        storeService.update(
            id,
            storeEditForm.getName(),
            storeEditForm.getAddress(),
            storeEditForm.getDescription(),
            storeEditForm.getStoreCategoryIds()
        );  
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "店舗情報の更新が完了しました");
        return "redirect:/stores/detail/" + id;
    }

    // 店舗画像の編集
    @GetMapping("/edit_image/{id}")    
    public String editImage(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("editImageForm") EditImageForm editImageForm,
        @PathVariable("id")  Integer id,
        Model model        
    ) {
        Store store = storeService.findById(id).orElseThrow();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("store", store);
        model.addAttribute("title", "画像の変更");
        model.addAttribute("main", "stores/edit_image::main");
        return "layout/logged_in";    
    }

    // 店舗画像の編集処理 
    @PostMapping("/update_image/{id}")
    public String updateImage(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Integer id,
        @Valid EditImageForm editImageForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model){
        if(bindingResult.hasErrors()){
            return editImage(loginUser, editImageForm, id, model);
        }
        Store store = storeService.findById(id).orElseThrow();
        storeService.updateImage(store.getId(), editImageForm.getImage());
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "画像の変更が完了しました");
        return "redirect:/stores/detail/" + id;
    }

    // 店舗の削除
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasStore(#id)")
    @PostMapping("/delete/{id}")    
    public String delete(
        @PathVariable("id")  Integer id,
        RedirectAttributes redirectAttributes,
        Model model) {
        storeService.delete(id);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "店舗の削除が完了しました");
        return "redirect:/owner/stores/";  
    }

}

