package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepo;

    public void addToCart(Cart cart) {
        cartRepo.save(cart);
    }

    public List<Cart> getUserCart(User user) {
        return cartRepo.findByUser(user);
    }

    public void removeCartItem(int id) {
        cartRepo.deleteById(id);
    }

    public void clearCart(User user) {
        List<Cart> items = cartRepo.findByUser(user);
        cartRepo.deleteAll(items);
    }
}
