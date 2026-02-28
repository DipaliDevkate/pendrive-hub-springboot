package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    // Register new user
    public void registerUser(User user) {
        userRepo.save(user);
    }

    // User login
    public User login(String email, String password) {
        return userRepo.findByEmailAndPassword(email, password);
    }

    // Get cart items
    public List<Cart> getCartItems(User user) {
        return cartRepo.findByUser(user);
    }

    // Get orders
    public List<Order> getOrders(User user) {
        return orderRepo.findByUser(user);
    }

    // Remove cart item
    public void removeCartItem(int cartId) {
        cartRepo.deleteById(cartId);
    }

    // Checkout all cart items
    public void checkoutCart(User user) {
        List<Cart> cartItems = cartRepo.findByUser(user);
        for (Cart item : cartItems) {
            buyNow(item);
        }
    }

    // Add product to cart
    public void addToCart(User user, int productId) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) return;

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(1);
        cart.setTotalAmount(product.getFinalPrice());

        cartRepo.save(cart);
    }

    // Buy a cart item
    public void buyNow(Cart cartItem) {
        if (cartItem == null) return;

        Order order = new Order();
        order.setUser(cartItem.getUser());
        order.setProduct(cartItem.getProduct());
        order.setQuantity(cartItem.getQuantity());
        order.setTotalAmount(cartItem.getTotalAmount());
        order.setOrderDate(LocalDateTime.now());

        orderRepo.save(order);
        cartRepo.delete(cartItem);
    }

    // Buy a product directly
    public void buyNowProduct(User user, Product product) {
        if (user == null || product == null) return;

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(1);
        order.setTotalAmount(product.getFinalPrice());
        order.setOrderDate(LocalDateTime.now());

        orderRepo.save(order);
    }

    // Get cart item by ID
    public Cart getCartById(int cartId) {
        return cartRepo.findById(cartId).orElse(null);
    }
}