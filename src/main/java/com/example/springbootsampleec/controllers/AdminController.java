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

    // ????????????
    @GetMapping("/")
    public String index(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "??????");
        model.addAttribute("main", "admins/index::main");
        return "layout/logged_in";    
    }

    // ??????????????????
    @GetMapping("/users/")    
    public String manageUsers(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<User> users = userService.findAll();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("users", users);
        model.addAttribute("title", "?????????????????????");
        model.addAttribute("main", "admins/manage_users::main");
        return "layout/logged_in";    
    }

    // ?????????????????????
    @GetMapping("/users/register_owner")    
    public String registerOwner(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("registerOwnerForm") RegisterOwnerForm registerOwnerForm,
        Model model
        ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "??????????????????");
        model.addAttribute("main", "admins/register_owner::main");
        return "layout/logged_in";    
    }

    // ??????????????????????????? 
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
            "??????????????????????????????????????????");
        return "redirect:/admin/users/";
    }

    // ???????????????????????????
    @GetMapping("/item_categories/")    
    public String manageItemCategories(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<ItemCategory> itemCategories = itemCategoryRepository.findAll();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("itemCategories", itemCategories);
        model.addAttribute("title", "??????????????????????????????");
        model.addAttribute("main", "admins/manage_item_categories::main");
        return "layout/logged_in";    
    }

    // ??????????????????????????????
    @GetMapping("/item_categories/add")    
    public String addItemCategory(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("itemCategoryAddForm") ItemCategoryAddForm itemCategoryAddForm,
        Model model
        ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "??????????????????????????????");
        model.addAttribute("main", "admins/item_category_add::main");
        return "layout/logged_in";    
    }

    // ???????????????????????????????????? 
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
            "???????????????????????????????????????????????????");
        return "redirect:/admin/item_categories/";
    }

    // ??????????????????????????????
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
        model.addAttribute("title", "??????????????????????????????");
        model.addAttribute("main", "admins/item_category_edit::main");
        return "layout/logged_in";    
    }

    // ????????????????????????????????????
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
            "???????????????????????????????????????????????????");
        return "redirect:/admin/item_categories/";
    }

    // ??????????????????????????????
    @PostMapping("/item_categories/delete/{id}")
    public String deleteItemCategory(
        @PathVariable("id")  long id,
        RedirectAttributes redirectAttributes,
        Model model) {
        itemCategoryService.delete(id);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "???????????????????????????????????????????????????");
        return "redirect:/admin/item_categories/";
    }

    // ???????????????
    @GetMapping("/nutrients/")    
    public String manageNutrients(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<Nutrient> nutrients = nutrientRepository.findAll();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("nutrients", nutrients);
        model.addAttribute("title", "??????????????????");
        model.addAttribute("main", "admins/manage_nutrients::main");
        return "layout/logged_in";    
    }

    // ??????????????????
    @GetMapping("/nutrients/add")    
    public String addNutrient(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("nutrientAddForm") NutrientAddForm nutrientAddForm,
        Model model
        ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "?????????????????????");
        model.addAttribute("main", "admins/nutrient_add::main");
        return "layout/logged_in";    
    }

    // ???????????????????????? 
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
            "???????????????????????????????????????");
        return "redirect:/admin/nutrients/";
    }

    // ??????????????????
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
        model.addAttribute("title", "??????????????????");
        model.addAttribute("main", "admins/nutrient_edit::main");
        return "layout/logged_in";    
    }

    // ????????????????????????
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
            "???????????????????????????????????????");
        return "redirect:/admin/nutrients/";
    }

    // ??????????????????
    @PostMapping("/nutrients/delete/{id}")
    public String deleteNutrient(
        @PathVariable("id")  long id,
        RedirectAttributes redirectAttributes,
        Model model) {
        nutrientService.delete(id);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "???????????????????????????????????????");
        return "redirect:/admin/nutrients/";
    }

    // ???????????????????????????
    @GetMapping("/store_categories/")    
    public String manageStoreCategories(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        List<StoreCategory> storeCategories = storeCategoryRepository.findAll();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("storeCategories", storeCategories);
        model.addAttribute("title", "??????????????????????????????");
        model.addAttribute("main", "admins/manage_store_categories::main");
        return "layout/logged_in";    
    }

    // ??????????????????????????????
    @GetMapping("/store_categories/add")    
    public String addStoreCategory(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("storeCategoryAddForm") StoreCategoryAddForm storeCategoryAddForm,
        Model model
        ) {
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "??????????????????????????????");
        model.addAttribute("main", "admins/store_category_add::main");
        return "layout/logged_in";    
    }

    // ???????????????????????????????????? 
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
            "???????????????????????????????????????????????????");
        return "redirect:/admin/store_categories/";
    }

    // ??????????????????????????????
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
        model.addAttribute("title", "??????????????????????????????");
        model.addAttribute("main", "admins/store_category_edit::main");
        return "layout/logged_in";    
    }

    // ????????????????????????????????????
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
            "???????????????????????????????????????????????????");
        return "redirect:/admin/store_categories/";
    }

    // ??????????????????????????????
    @PostMapping("/store_categories/delete/{id}")
    public String deleteStoreCategory(
        @PathVariable("id")  long id,
        RedirectAttributes redirectAttributes,
        Model model) {
        storeCategoryService.delete(id);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "???????????????????????????????????????????????????");
        return "redirect:/admin/store_categories/";
    }
    
}