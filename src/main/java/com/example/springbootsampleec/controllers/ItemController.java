package com.example.springbootsampleec.controllers;
 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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

import com.example.springbootsampleec.entities.Item;
import com.example.springbootsampleec.entities.ItemCategory;
import com.example.springbootsampleec.entities.Nutrient;
import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.forms.EditImageForm;
import com.example.springbootsampleec.forms.ItemEditForm;
import com.example.springbootsampleec.forms.RegisterItemForm;
import com.example.springbootsampleec.repositories.ItemCategoryRepository;
import com.example.springbootsampleec.repositories.ItemRepository;
import com.example.springbootsampleec.repositories.NutrientRepository;
import com.example.springbootsampleec.repositories.StoreRepository;
import com.example.springbootsampleec.services.ItemService;
import com.example.springbootsampleec.services.SessionService;
import com.example.springbootsampleec.services.StoreService;
 
@RequestMapping("/items")
@Controller
public class ItemController { 

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private NutrientRepository nutrientRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreService storeService;
    @Autowired
    private SessionService sessionService;
    
    // ????????????
    @GetMapping("/{storeId}")    
    public String index(
        HttpServletRequest request,
        @PathVariable("storeId") Long storeId,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {        
        Store store = storeRepository.findById(storeId).orElseThrow();
        List<Item> items = itemRepository.findByStoreId(storeId);
        List<Item> adminItems = itemRepository.findByStoreUserRolesContaining("ROLE_ADMIN");
        List<ItemCategory> itemCategories = itemCategoryRepository.findAll();
        List<Nutrient> nutrients = nutrientRepository.findAll();
        sessionService.setUri(request);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("store", store);
        model.addAttribute("items", items);
        model.addAttribute("adminItems", adminItems);
        model.addAttribute("itemCategories", itemCategories);
        model.addAttribute("nutrients", nutrients);
        model.addAttribute("title", "????????????");
        model.addAttribute("main", "items/index::main");
        return "layout/logged_in";    
    }

    //???????????? ?????????????????????
	@PostMapping("/{storeId}")
	public String indexFiltered(
        @PathVariable("storeId") Long storeId,
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam(value = "name", required = false) String name,
        Model model
    ) {
        List<Item> items = new ArrayList<Item>();
        if (id == 0) {
            items = itemRepository.findByStoreId(storeId);
        } else {
            if(name.equals("category")) {
                items = itemRepository.findByItemCategoriesIdAndStoreId(id, storeId);
            } else if (name.equals("nutrient")) {
                items = itemRepository.findByNutrientsIdAndStoreId(id, storeId);
            } else {
                items = itemRepository.findByStoreId(storeId);
            }            
        }
        model.addAttribute("items", items);
        return "items/index::itemView";
	}

    // ???????????????
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasStore(#storeId)")
    @GetMapping("/register/{storeId}")    
    public String register(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("storeId")  Long storeId,
        @ModelAttribute("registerItemForm") RegisterItemForm registerItemForm,
        Model model
    ) {
        Store store = storeService.findById(storeId).orElseThrow();
        model.addAttribute("title", "?????????????????????");
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("store", store);
        model.addAttribute("itemCategories", itemCategoryRepository.findAll());
        model.addAttribute("nutrients", nutrientRepository.findAll());
        model.addAttribute("main", "items/register::main");
        return "layout/logged_in";    
    }
    
    // ?????????????????????
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasStore(#storeId)")
    @PostMapping("/register/{storeId}")    
    public String registerProcess(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("storeId")  Long storeId,
        @Valid RegisterItemForm registerItemForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
        ) {
        if(bindingResult.hasErrors()){
            return register(loginUser, storeId, registerItemForm, model);
        }
        Store store = storeService.findById(storeId).orElseThrow();
        itemService.register(
            store,
            registerItemForm.getName(),
            registerItemForm.getPrice(),
            registerItemForm.getCalorie(), 
            registerItemForm.getItemCategoryIds(),
            registerItemForm.getNutrientIds(),
            registerItemForm.getDescription(),
            registerItemForm.getImage()
        );
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "???????????????????????????");
        return "redirect:/owner/items/" + storeId;
    }

    // ???????????????
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasItem(#id)")
    @GetMapping("/detail/{id}")    
    public String detail(
        HttpServletRequest request,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Long id,
        Model model
    ) {
        Item item = itemService.findById(id).orElseThrow();
        sessionService.setUri(request);
        model.addAttribute("item", item);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "???????????????");
        model.addAttribute("main", "items/detail::main");
        return "layout/logged_in";    
    }
    
    // ???????????????
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasItem(#id)")
    @GetMapping("/edit/{id}")    
    public String edit(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("itemEditForm") ItemEditForm itemEditForm,
        @PathVariable("id")  Integer id,
        Model model
    ) {
        Item item = itemService.findById(id).orElseThrow();
        // ???????????????????????????????????????????????????ID?????????
        List<ItemCategory> itemCategories = itemCategoryRepository.findByCategorizedItemsId(item.getId());
        Set<Long> itemCategoryIds = new HashSet<>();
        for (ItemCategory itemCategory : itemCategories) itemCategoryIds.add(itemCategory.getId());
        // ???????????????????????????????????????ID?????????
        List<Nutrient> nutrients = nutrientRepository.findByItemsId(item.getId());
        Set<Long> nutrientIds = new HashSet<>();
        for (Nutrient nutrient : nutrients) nutrientIds.add(nutrient.getId());
        itemEditForm.setName(item.getName());
        itemEditForm.setPrice(item.getPrice());
        itemEditForm.setCalorie(item.getCalorie());
        itemEditForm.setItemCategoryIds(itemCategoryIds);
        itemEditForm.setNutrientIds(nutrientIds);
        itemEditForm.setDescription(item.getDescription());
        model.addAttribute("item", item);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("itemCategories", itemCategoryRepository.findAll());
        model.addAttribute("nutrients", nutrientRepository.findAll());
        model.addAttribute("title", "???????????????");
        model.addAttribute("main", "items/edit::main");
        return "layout/logged_in";    
    }
    
    // ?????????????????????
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasItem(#id)")
    @PostMapping("/update/{id}")    
    public String update(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Integer id,
        @Valid ItemEditForm itemEditForm,
        RedirectAttributes redirectAttributes,
        BindingResult bindingResult,
        Model model) {
        if(bindingResult.hasErrors()){
            return edit(loginUser, itemEditForm, id, model);
        }
        itemService.updateItem(
            id,
            itemEditForm.getName(),
            itemEditForm.getPrice(),
            itemEditForm.getCalorie(),
            itemEditForm.getItemCategoryIds(),
            itemEditForm.getNutrientIds(),
            itemEditForm.getDescription()
        );  
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "??????????????????????????????????????????");
        return "redirect:/items/detail/" + id;
    }

    // ?????????????????????
    @GetMapping("/edit_image/{id}")    
    public String editImage(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("editImageForm") EditImageForm editImageForm,
        @PathVariable("id")  Integer id,
        Model model        
    ) {
        Item item = itemService.findById(id).orElseThrow();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("item", item);
        model.addAttribute("title", "???????????????");
        model.addAttribute("main", "items/edit_image::main");
        return "layout/logged_in";    
    }

    // ??????????????????????????? 
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
        Item item = itemService.findById(id).orElseThrow();
        itemService.updateImage(item.getId(), editImageForm.getImage());
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "????????????????????????????????????");
        return "redirect:/items/detail/" + id;
    }
    
    // ???????????????
    @PreAuthorize("hasRole('ADMIN') or @customAuthorizer.hasItem(#id)")
    @PostMapping("/delete/{id}")
    public String delete(
        @PathVariable("id")  Integer id,
        RedirectAttributes redirectAttributes,
        Model model) {
        Store store = storeService.findByItemsId(id);
        itemService.delete(id);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "????????????????????????????????????");
        return "redirect:/owner/items/" + store.getId();
    }
}