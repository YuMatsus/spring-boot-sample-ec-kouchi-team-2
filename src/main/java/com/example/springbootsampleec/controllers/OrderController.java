package com.example.springbootsampleec.controllers;
 
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springbootsampleec.entities.CartItem;
import com.example.springbootsampleec.entities.Order;
import com.example.springbootsampleec.entities.Store;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.forms.OrderForm;
import com.example.springbootsampleec.repositories.CartItemRepository;
import com.example.springbootsampleec.repositories.OrderRepository;
import com.example.springbootsampleec.services.CartService;
import com.example.springbootsampleec.services.GeoApiService;
import com.example.springbootsampleec.services.SessionService;
 
@RequestMapping("/order")
@Controller
public class OrderController { 

    @Autowired
    CartService cartService;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private GeoApiService geoApiService;
    
    // 注文フォーム用ラジオボタン
    private Map<String, String> radioPaymentMethod;
    private Map<String, String> initRadioPaymentMethod() {
        Map<String, String> radio = new LinkedHashMap<>();
        radio.put("0", "代金引換");
        return radio;
    }
    private Map<String, String> radioDeliveryPreservation;
    private Map<String, String> initRadioDeliveryPreservation() {
        Map<String, String> radio = new LinkedHashMap<>();
        radio.put("0", "即時配達");
        radio.put("1", "時間指定");
        return radio;
    }

    // 注文ページ
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    @GetMapping("/{id}")
    public String order(
        @ModelAttribute("orderForm") OrderForm orderForm,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id") Long id,
        Model model
    ) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(id);
        Store store = cartItems.get(0).getItem().getStore();

        // 配達予定時刻の算出
        LocalDateTime deliveryTime = 
            LocalDateTime.now() // 現在時刻
            .plusSeconds(geoApiService.getRequiredTimeStoreToUser(store, loginUser)) // 配達に要する時間
            .plusSeconds(1800) // 調理に要する時間(30分固定)
        ;
        orderForm.setDeliveryTime(deliveryTime);

        radioPaymentMethod = initRadioPaymentMethod();
        radioDeliveryPreservation = initRadioDeliveryPreservation();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("radioPaymentMethod", radioPaymentMethod);
        model.addAttribute("radioDeliveryPreservation", radioDeliveryPreservation);
        model.addAttribute("deliveryTime", deliveryTime);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("title", "注文");
        model.addAttribute("main","users/order::main");
        return "layout/logged_in";
    }

    // 注文処理
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.user.id")
    @PostMapping("/{id}")    
    public String orderProcess(
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id")  Long id,
        @Valid OrderForm orderForm,
        RedirectAttributes redirectAttributes,
        BindingResult bindingResult,
        Model model) {
        if(bindingResult.hasErrors()){
            return order(orderForm, loginUser, id, model);
        }
        List<CartItem> cartItems = cartItemRepository.findByUserId(id);
        cartService.order(loginUser, cartItems);
        sessionService.updateSecurityContext(loginUser);

        redirectAttributes.addFlashAttribute(
            "deliveryTime",
            orderForm.getDeliveryTime()
        );
        redirectAttributes.addFlashAttribute("successMessage", "注文を承りました。");
        return "redirect:/order/complete/" + id;
    }

    // 注文完了
    @GetMapping("/complete/{id}")    
    public String orderComplete(
        @ModelAttribute("deliveryTime") LocalDateTime deliveryTime,
        @ModelAttribute("successMessage") String successMessage,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        @PathVariable("id") Long id,
        Model model
    ) { 
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("deliveryTime", deliveryTime);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("title", "注文完了");
        model.addAttribute("main", "users/order_complete::main");
        return "layout/logged_in";    
    }

    // 注文履歴
    @GetMapping("/history/{userId}")    
    public String orderHistory(
        @PathVariable("userId") Long userId,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {        
        List<Order> orders = orderRepository.findByUserId(userId);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("orders", orders);
        model.addAttribute("title", "注文履歴");
        model.addAttribute("main", "users/order_history::main");
        return "layout/logged_in";    
    }

    // 注文履歴詳細
    @GetMapping("/history/detail/{id}")    
    public String orderHistoryDetail(
        @PathVariable("id") Long id,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {        
        Order order = orderRepository.findById(id).orElseThrow();
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("order", order);
        model.addAttribute("title", "注文履歴");
        model.addAttribute("main", "users/order_history_detail::main");
        return "layout/logged_in";    
    }

    // 過去の注文からカートに追加
    @PostMapping("/add_cart/{id}")
    public String add(
        @PathVariable("id")  Long id,
        RedirectAttributes redirectAttributes,
        @AuthenticationPrincipal(expression = "user") User loginUser,
        Model model
    ) {
        Order order = orderRepository.findById(id).orElseThrow();
        cartService.addFromOrderHistory(order);
        sessionService.updateSecurityContext(loginUser);
        redirectAttributes.addFlashAttribute(
            "successMessage",
            "カートに追加しました");
        return "redirect:/cart/" + loginUser.getId();
    }

}