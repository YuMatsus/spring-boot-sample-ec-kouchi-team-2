package com.example.springbootsampleec.services.impl;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springbootsampleec.entities.CartItem;
import com.example.springbootsampleec.entities.Item;
import com.example.springbootsampleec.entities.Order;
import com.example.springbootsampleec.entities.OrderedItem;
import com.example.springbootsampleec.entities.User;
import com.example.springbootsampleec.exceptions.ExistOtherStoreCartItemException;
import com.example.springbootsampleec.repositories.CartItemRepository;
import com.example.springbootsampleec.repositories.ItemRepository;
import com.example.springbootsampleec.repositories.OrderRepository;
import com.example.springbootsampleec.repositories.OrderedItemRepository;
import com.example.springbootsampleec.repositories.StoreRepository;
import com.example.springbootsampleec.repositories.UserRepository;
import com.example.springbootsampleec.services.CartService;

 
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderedItemRepository orderedItemRepository;

    // カートに追加
    @Transactional
    @Override
    public void add(Long userId, Long itemId){
        User user = userRepository.findById(userId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();

        // 既に別店舗の料理がカートに入っている場合は例外を投げる。
        // カートに追加しようとするアイテムがオーナーによって登録されたものである場合にのみ判定を行う。
        if(item.getStore().getUser().getRoles().contains("ROLE_OWNER")){
            List<CartItem> cartItems = cartItemRepository.findByUserIdAndItemStoreIdNot(userId, item.getStore().getId());
            for (CartItem cartItem : cartItems) {
                // 判定対象はオーナーによって登録された料理のみとする。
                if(cartItem.getItem().getStore().getUser().getRoles().contains("ROLE_OWNER")){
                    throw new ExistOtherStoreCartItemException("別の店舗の料理がカートに入っています。");
                }
            }
        }

        if(cartItemRepository.existsByUserIdAndItemId(userId, itemId)){
        } else {
            CartItem cartItem = new CartItem(null, user, item, 1);
            cartItemRepository.saveAndFlush(cartItem);
        }
    };

    // カートから削除
    @Transactional
    @Override
    public void delete(Long userId, Long itemId) {
        if(cartItemRepository.existsByUserIdAndItemId(userId, itemId)){
            CartItem cartItem = cartItemRepository.findByUserIdAndItemId(userId, itemId);
            cartItemRepository.delete(cartItem);
        } else {     
        }    
    }

    // 数量の変更
    @Transactional
    @Override
    public void updateQuantity(Long id, int quantity) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        cartItem.setQuantity(quantity);
        cartItemRepository.saveAndFlush(cartItem);
    }

    // 注文処理
    @Transactional
    @Override
    public void order(User user, List<CartItem> cartItems) {
		addOrderToHistory(user, cartItems);
        emptyCart(user);
    }

    // カートを空にする
    @Transactional
    @Override
    public void emptyCart(User user) {
        cartItemRepository.deleteByUserId(user.getId());
    }

    // 注文履歴からカートに追加
    @Transactional
    @Override
    public void addFromOrderHistory(Order order) {
        User user = order.getUser();
        List<OrderedItem> orderedItems = orderedItemRepository.findByOrderId(order.getId());
        for (OrderedItem orderedItem : orderedItems) {
            Item item = orderedItem.getItem();
            if(cartItemRepository.existsByUserIdAndItemId(user.getId(), item.getId())){
            } else {
                CartItem cartItem = new CartItem(null, user, item, orderedItem.getQuantity());
                cartItemRepository.saveAndFlush(cartItem);
            }
        }      
    }

    // 購入履歴に注文を追加
	private void addOrderToHistory(User user, List<CartItem> cartItems){
        Order order = new Order(null, user, null, null, null);
        orderRepository.saveAndFlush(order);
        for (CartItem cartItem : cartItems) {
            Item item = cartItem.getItem();
            OrderedItem orderedItem = new OrderedItem(null, order, item, cartItem.getQuantity());
            orderedItemRepository.saveAndFlush(orderedItem);
        }
	}
    
}