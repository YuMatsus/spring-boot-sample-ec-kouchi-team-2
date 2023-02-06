package com.example.springbootsampleec.controllers;
 
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springbootsampleec.entities.ItemCategory;
import com.example.springbootsampleec.entities.Nutrient;
import com.example.springbootsampleec.entities.StoreCategory;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.forms.ItemCategoryAddForm;
import com.example.springbootsampleec.forms.ItemCategoryEditForm;
import com.example.springbootsampleec.forms.NutrientAddForm;
import com.example.springbootsampleec.forms.NutrientEditForm;
import com.example.springbootsampleec.forms.RegisterOwnerForm;
import com.example.springbootsampleec.forms.StoreCategoryAddForm;
import com.example.springbootsampleec.forms.StoreCategoryEditForm;
import com.example.springbootsampleec.repositories.ItemCategoryRepository;
import com.example.springbootsampleec.repositories.NutrientRepository;
import com.example.springbootsampleec.repositories.StoreCategoryRepository;
import com.example.springbootsampleec.services.ItemCategoryService;
import com.example.springbootsampleec.services.NutrientService;
import com.example.springbootsampleec.services.StoreCategoryService;
import com.example.springbootsampleec.services.UserService;
 
@RequestMapping("/admin")
@Controller
public class AdminController { 
    @Autowired
    private UserService userService;
    @Autowired
    private ItemCategoryService itemCategoryService;
    @Autowired
    private NutrientService nutrientService;
    @Autowired
    private StoreCategoryService storeCategoryService;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private NutrientRepository nutrientRepository;
    @Autowired StoreCategoryRepository storeCategoryRepository;

    // 管理画面
    @GetMapping("/")
    public String index(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "管理");
        model.addAttribute("main", "admins/index::main");
        return "layout/logged_in";    
    }

    // ユーザー一覧
    @GetMapping("/users/")    
    public String manageUsers(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<User> users = userService.findAll();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("users", users);
        model.addAttribute("title", "ユーザーの管理");
        model.addAttribute("main", "admins/manage_users::main");
        return "layout/logged_in";    
    }

    // オーナーの登録
    @GetMapping("/users/register_owner")    
    public String registerOwner(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("registerOwnerForm") RegisterOwnerForm registerOwnerForm,
        Model model
        ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "オーナー登録");
        model.addAttribute("main", "admins/register_owner::main");
        return "layout/logged_in";    
    }

    // オーナーの登録処理 
    @PostMapping("/users/register_owner")
    public String registerOwnerProcess(
        @ModelAttribute("registerOwnerForm") RegisterOwnerForm registerOwnerForm,
        RedirectAttributes redirectAttributes,
        Model model){
        String[] roles = {"ROLE_OWNER"};
        userService.register(
            registerOwnerForm.getName(),
            registerOwnerForm.getEmail(),
            registerOwnerForm.getPassword(),
            registerOwnerForm.getAddress(),
            roles);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "オーナーの登録が完了しました");
        return "redirect:/admin/users/";
    }

    // 店舗カテゴリー一覧
    @GetMapping("/item_categories/")    
    public String manageItemCategories(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<ItemCategory> itemCategories = itemCategoryRepository.findAll();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("itemCategories", itemCategories);
        model.addAttribute("title", "料理カテゴリーの管理");
        model.addAttribute("main", "admins/manage_item_categories::main");
        return "layout/logged_in";    
    }

    // 料理カテゴリーの追加
    @GetMapping("/item_categories/add")    
    public String addItemCategory(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("itemCategoryAddForm") ItemCategoryAddForm itemCategoryAddForm,
        Model model
        ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "料理カテゴリーの追加");
        model.addAttribute("main", "admins/item_category_add::main");
        return "layout/logged_in";    
    }

    // 料理カテゴリーの追加処理 
    @PostMapping("/item_categories/add")
    public String addItemCategoryProcess(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @Valid ItemCategoryAddForm itemCategoryAddForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        if(bindingResult.hasErrors()){
            return addItemCategory(loginUser, itemCategoryAddForm, model);
        }
        itemCategoryService.register(itemCategoryAddForm.getName());
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "料理カテゴリーの登録が完了しました");
        return "redirect:/admin/item_categories/";
    }

    // 料理カテゴリーの編集
    @GetMapping("/item_categories/edit/{id}")    
    public String editItemCategory(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("itemCategoryEditForm") ItemCategoryEditForm itemCategoryEditForm,
        @PathVariable("id")  long id,
        Model model) {
        ItemCategory itemCategory = itemCategoryRepository.findById(id).orElseThrow();
        itemCategoryEditForm.setName(itemCategory.getName());
        model.addAttribute("itemCategory", itemCategory);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "料理カテゴリーの編集");
        model.addAttribute("main", "admins/item_category_edit::main");
        return "layout/logged_in";    
    }

    // 料理カテゴリーの編集処理
    @PostMapping("/item_categories/edit/{id}")    
    public String editItemCategoryProcess(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  long id,
        @Valid ItemCategoryEditForm itemCategoryEditForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model) {
        if(bindingResult.hasErrors()){
            return editItemCategory(loginUser, itemCategoryEditForm, id, model);
        }
        itemCategoryService.update(
            id,
            itemCategoryEditForm.getName()
        );  
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "料理カテゴリーの更新が完了しました");
        return "redirect:/admin/item_categories/";
    }

    // 料理カテゴリーの削除
    @PostMapping("/item_categories/delete/{id}")
    public String deleteItemCategory(
        @PathVariable("id")  long id,
        RedirectAttributes redirectAttributes,
        Model model) {
        itemCategoryService.delete(id);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "料理カテゴリーの削除が完了しました");
        return "redirect:/admin/item_categories/";
    }

    // 栄養素一覧
    @GetMapping("/nutrients/")    
    public String manageNutrients(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<Nutrient> nutrients = nutrientRepository.findAll();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("nutrients", nutrients);
        model.addAttribute("title", "栄養素の管理");
        model.addAttribute("main", "admins/manage_nutrients::main");
        return "layout/logged_in";    
    }

    // 栄養素の追加
    @GetMapping("/nutrients/add")    
    public String addNutrient(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("nutrientAddForm") NutrientAddForm nutrientAddForm,
        Model model
        ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "栄養素ーの追加");
        model.addAttribute("main", "admins/nutrient_add::main");
        return "layout/logged_in";    
    }

    // 栄養素の追加処理 
    @PostMapping("/nutrients/add")
    public String addNutrientProcess(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @Valid NutrientAddForm nutrientAddForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        if(bindingResult.hasErrors()){
            return addNutrient(loginUser, nutrientAddForm, model);
        }
        nutrientService.register(nutrientAddForm.getName());
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "栄養素の登録が完了しました");
        return "redirect:/admin/nutrients/";
    }

    // 栄養素の編集
    @GetMapping("/nutrients/edit/{id}")    
    public String editNutrient(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("nutrientEditForm") NutrientEditForm nutrientEditForm,
        @PathVariable("id")  long id,
        Model model) {
        Nutrient nutrient = nutrientRepository.findById(id).orElseThrow();
        nutrientEditForm.setName(nutrient.getName());
        model.addAttribute("nutrient", nutrient);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "栄養素の編集");
        model.addAttribute("main", "admins/nutrient_edit::main");
        return "layout/logged_in";    
    }

    // 栄養素の編集処理
    @PostMapping("/nutrients/edit/{id}")    
    public String editNutrientProcess(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  long id,
        @Valid NutrientEditForm nutrientEditForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model) {
        if(bindingResult.hasErrors()){
            return editNutrient(loginUser, nutrientEditForm, id, model);
        }
        nutrientService.update(
            id,
            nutrientEditForm.getName()
        );  
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "栄養素の更新が完了しました");
        return "redirect:/admin/nutrients/";
    }

    // 栄養素の削除
    @PostMapping("/nutrients/delete/{id}")
    public String deleteNutrient(
        @PathVariable("id")  long id,
        RedirectAttributes redirectAttributes,
        Model model) {
        nutrientService.delete(id);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "栄養素の削除が完了しました");
        return "redirect:/admin/nutrients/";
    }

    // 店舗カテゴリー一覧
    @GetMapping("/store_categories/")    
    public String manageStoreCategories(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<StoreCategory> storeCategories = storeCategoryRepository.findAll();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("storeCategories", storeCategories);
        model.addAttribute("title", "店舗カテゴリーの管理");
        model.addAttribute("main", "admins/manage_store_categories::main");
        return "layout/logged_in";    
    }

    // 店舗カテゴリーの追加
    @GetMapping("/store_categories/add")    
    public String addStoreCategory(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("storeCategoryAddForm") StoreCategoryAddForm storeCategoryAddForm,
        Model model
        ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "店舗カテゴリーの追加");
        model.addAttribute("main", "admins/store_category_add::main");
        return "layout/logged_in";    
    }

    // 店舗カテゴリーの追加処理 
    @PostMapping("/store_categories/add")
    public String addStoreCategoryProcess(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @Valid StoreCategoryAddForm storeCategoryAddForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        if(bindingResult.hasErrors()){
            return addStoreCategory(loginUser, storeCategoryAddForm, model);
        }
        storeCategoryService.register(storeCategoryAddForm.getName());
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "店舗カテゴリーの登録が完了しました");
        return "redirect:/admin/store_categories/";
    }

    // 店舗カテゴリーの編集
    @GetMapping("/store_categories/edit/{id}")    
    public String editStoreCategory(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("storeCategoryEditForm") StoreCategoryEditForm storeCategoryEditForm,
        @PathVariable("id")  long id,
        Model model) {
        StoreCategory storeCategory = storeCategoryRepository.findById(id).orElseThrow();
        storeCategoryEditForm.setName(storeCategory.getName());
        model.addAttribute("storeCategory", storeCategory);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "店舗カテゴリーの編集");
        model.addAttribute("main", "admins/store_category_edit::main");
        return "layout/logged_in";    
    }

    // 店舗カテゴリーの編集処理
    @PostMapping("/store_categories/edit/{id}")    
    public String editStoreCategoryProcess(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  long id,
        @Valid StoreCategoryEditForm storeCategoryEditForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model) {
        if(bindingResult.hasErrors()){
            return editStoreCategory(loginUser, storeCategoryEditForm, id, model);
        }
        storeCategoryService.update(
            id,
            storeCategoryEditForm.getName()
        );  
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "店舗カテゴリーの更新が完了しました");
        return "redirect:/admin/store_categories/";
    }

    // 店舗カテゴリーの削除
    @PostMapping("/store_categories/delete/{id}")
    public String deleteStoreCategory(
        @PathVariable("id")  long id,
        RedirectAttributes redirectAttributes,
        Model model) {
        storeCategoryService.delete(id);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "店舗カテゴリーの削除が完了しました");
        return "redirect:/admin/store_categories/";
    }
    
}