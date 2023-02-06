package com.example.springbootsampleec.controllers;
 
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springbootsampleec.entities.CartItem;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.repositories.CartItemRepository;
import com.example.springbootsampleec.services.CartService;
import com.example.springbootsampleec.services.SessionService;
import com.example.springbootsampleec.services.UserService;
 
@RequestMapping("/cart")
@Controller
public class CartController { 

    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartService cartService;

    // ショッピングカートページ
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    @GetMapping("/{id}")
    public String index(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id") Long id,
        Model model
    ) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(id);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "ショッピングカート");
        model.addAttribute("main","users/cart::main");
        return "layout/logged_in";
    }

    // 追加
    @PostMapping("/add/{itemId}")
    public String add(
        HttpServletRequest request,
        @PathVariable("itemId")  Long itemId,
        RedirectAttributes redirectAttributes,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        cartService.add(loginUser.getId(), itemId);
        sessionService.updateSecurityContext(loginUser);
        String uri = sessionService.getUri(request);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "カートに追加しました");
        return "redirect:" + uri;
    }

    // 削除
    @PostMapping("/delete/{itemId}")
    public String delete(
        @PathVariable("itemId")  Long itemId,
        RedirectAttributes redirectAttributes,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model) {
        cartService.delete(loginUser.getId(), itemId);
        sessionService.updateSecurityContext(loginUser);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "カートから料理を削除しました");
        return "redirect:/cart/" + loginUser.getId();
    }

    // 数量を変更
    @PostMapping("/update_quantity/{id}")
    public String updateQuantity(
        @PathVariable("id")  Long id,
        @RequestParam("quantity") int quantity,
        RedirectAttributes redirectAttributes,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        cartService.updateQuantity(id, quantity);
        sessionService.updateSecurityContext(loginUser);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "カートから料理の数量を変更しました");
        return "redirect:/cart/" + loginUser.getId();
    }

    
}