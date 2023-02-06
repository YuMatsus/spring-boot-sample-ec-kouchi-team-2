package com.example.springbootsampleec.controllers;
 
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springbootsampleec.entities.Item;
import com.example.springbootsampleec.entities.ItemCategory;
import com.example.springbootsampleec.entities.Nutrient;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.forms.PasswordEditForm;
import com.example.springbootsampleec.forms.SignUpForm;
import com.example.springbootsampleec.forms.UserEditForm;
import com.example.springbootsampleec.repositories.ItemCategoryRepository;
import com.example.springbootsampleec.repositories.NutrientRepository;
import com.example.springbootsampleec.services.ItemService;
import com.example.springbootsampleec.services.UserService;

@RequestMapping("/users")
@Controller
public class UserController { 
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private NutrientRepository nutrientRepository;
    
    // サインアップ画面
    @GetMapping("/sign_up")    
    public String signUp(
        @ModelAttribute("signUpForm") SignUpForm signUpForm,
        Model model
    ) {
        model.addAttribute("title", "新規会員登録");
        model.addAttribute("main", "users/sign_up::main");
        return "layout/not_logged_in";    
    }
    
    // サインアップフォーム投稿時の処理
    @PostMapping("/sign_up")
    public String signUpProcess(
        @Valid SignUpForm signUpForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model
    ){
        if(bindingResult.hasErrors()) {
			return signUp(signUpForm, model);
		}
        String[] roles = {"ROLE_USER"};
        userService.register(
            signUpForm.getName(),
            signUpForm.getEmail(),
            signUpForm.getPassword(),
            signUpForm.getAddress(),
            roles);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "アカウントの登録が完了しました");
        return "redirect:/users/sign_up/complete";
    }

    // サインアップ完了後のページ
    @GetMapping("/sign_up/complete")
    public String signUpComplete(Model model) {
        model.addAttribute("title", "サインアップの完了");
        model.addAttribute("main", "users/sign_up_complete::main");
        return "layout/not_logged_in";  
    }

    // ログイン画面
    @GetMapping("/login")    
    public String login(Model model) {

        // トップに表示する料理情報の取得
        List<Item> items = itemService.getRandomItems(12);		
		List<ItemCategory> itemCategories = itemCategoryRepository.findAll();
        List<Nutrient> nutrients = nutrientRepository.findAll();

		model.addAttribute("items", items);
		model.addAttribute("itemCategories", itemCategories);
        model.addAttribute("nutrients", nutrients);
        model.addAttribute("title", "ログイン");
        model.addAttribute("main", "users/login::main");
        return "layout/not_logged_in";    
    }

    // ユーザープロフィール
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    @GetMapping("/detail/{id}")    
    public String detail(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Long id,
        Model model
    ) {
        User user = userService.findById(id).orElseThrow();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("user", user);
        model.addAttribute("title", "ユーザープロフィール");
        model.addAttribute("main", "users/detail::main");
        return "layout/logged_in";    
    }

    // ユーザー情報の編集
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    @GetMapping("/edit/{id}")    
    public String edit(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("userEditForm") UserEditForm userEditForm,
        @PathVariable("id")  Long id,
        Model model
    ) {
        User user = userService.findById(id).orElseThrow();
        userEditForm.setName(user.getName());
        userEditForm.setEmail(user.getEmail());
        userEditForm.setAddress(user.getAddress());
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("user", user);
        model.addAttribute("title", "ユーザー情報を編集");
        model.addAttribute("main", "users/edit::main");
        return "layout/logged_in";    
    }

    // ユーザー情報の編集処理
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    @PostMapping("/update/{id}")    
    public String update(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Long id,
        @Valid UserEditForm userEditForm,
        RedirectAttributes redirectAttributes,
        BindingResult bindingResult,
        Model model) {
        if(bindingResult.hasErrors()){
            return edit(loginUser, userEditForm, id, model);
        }
        userService.update(
            id,
            userEditForm.getName(),
            userEditForm.getEmail(),
            userEditForm.getAddress()
        );  
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "ユーザー情報の更新が完了しました");
        return "redirect:/users/detail/" + id;
    }

    // パスワードの変更
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    @GetMapping("/edit_password/{id}")    
    public String editPassword(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @ModelAttribute("passwordEditForm") PasswordEditForm passwordEditForm,
        @PathVariable("id")  Long id,
        Model model
    ) {
        User user = userService.findById(id).orElseThrow();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("user", user);
        model.addAttribute("title", "パスワードの変更");
        model.addAttribute("main", "users/edit_password::main");
        return "layout/logged_in";    
    }

    // パスワードの変更処理
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    @PostMapping("/edit_password/{id}")    
    public String updatePassword(
        HttpServletRequest request,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Long id,
        @Valid PasswordEditForm passwordEditForm,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes,
        Model model) {
        if(bindingResult.hasErrors()){
            return editPassword(loginUser, passwordEditForm, id, model);
        }
        if (passwordEncoder.matches(passwordEditForm.getRawPassword(),loginUser.getPassword())){
            userService.updatePassword(loginUser, passwordEditForm.getNewPassword());
        }
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "パスワードの変更が完了しました");
        return "redirect:/users/detail/" + id;
    }

    // ユーザーの削除
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    @PostMapping("/delete/{id}")    
    public String delete(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Long id,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "ユーザーの削除が完了しました");
        return "redirect:/admin/users/";
    }
    
//    注文履歴詳細
    @GetMapping("/users/{history_id}")
    public String history(
    		@PathVariable("history_id") Integer id,
    		Model model
    		){
    	model.addAttribute("title", "注文履歴詳細");
    	model.addAttribute("history_id",id);
    	model.addAttribute("main", "users/histor::main");
    	return "layout/history_detail";
    }
}