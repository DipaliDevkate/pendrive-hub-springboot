package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.User;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepo;
    
    @Autowired
    private OrderRepository orderRepo;

    // ==================== LOGIN & REGISTRATION ====================

    // Show login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "user_login"; // matches user_login.html
    }

    // Handle login submission
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session) {
        User user = userService.login(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/user/dashboard";
        }
        return "redirect:/user/login?error";
    }

    // Show registration page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "user_register"; // matches user_register.html
    }

    // Handle registration submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/user/login";
    }

    // ==================== DASHBOARD ====================

    // Show user dashboard with cart and orders
    @GetMapping("/dashboard")
    public String userDashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/user/login";

        List<Cart> cartItems = userService.getCartItems(user);
        List<Order> orders = userService.getOrders(user);

        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("orders", orders);

        return "user_dashboard";
    }

    // ==================== CART OPERATIONS ====================

    // Add product to cart
    @GetMapping("/add-to-cart/{productId}")
    public String addToCart(@PathVariable int productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/user/login";

        userService.addToCart(user, productId);
        return "redirect:/user/dashboard";
    }

    // Remove item from cart
    @GetMapping("/cart/remove/{cartId}")
    public String removeCartItem(@PathVariable int cartId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) userService.removeCartItem(cartId);
        return "redirect:/user/dashboard";
    }

    // Checkout all cart items
    @GetMapping("/cart/checkout")
    public String checkoutCart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) userService.checkoutCart(user);
        return "redirect:/user/dashboard";
    }

    // Buy a single cart item immediately
    @GetMapping("/cart/buy-now/{cartId}")
    public String buyCartItem(@PathVariable int cartId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/user/login";

        Cart cartItem = userService.getCartById(cartId);
        if (cartItem != null) userService.buyNow(cartItem);

        return "redirect:/user/dashboard";
    }

    // ==================== DIRECT BUY ====================

    // Buy a product directly without adding to cart
    @GetMapping("/buy-now/{productId}")
    public String buyProductNow(@PathVariable int productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/user/login";

        Product product = productRepo.findById(productId).orElse(null);
        if (product != null) userService.buyNowProduct(user, product);

        return "redirect:/user/dashboard";
    }

    // ==================== LOGOUT ====================

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
    
 // Show buy-now form for a single product or cart item
    @GetMapping("/buy-now/form/{productId}")
    public String showBuyNowForm(@PathVariable int productId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/user/login";

        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) return "redirect:/user/dashboard";

        model.addAttribute("product", product);
        model.addAttribute("user", user);
        return "buy_now_form";
    }

    // Handle the form submission
    @PostMapping("/buy-now/confirm")
    public String confirmBuyNow(@RequestParam int productId,
                                @RequestParam int quantity,
                                @RequestParam String address,
                                @RequestParam String mobile,
                                HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/user/login";

        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) return "redirect:/user/dashboard";

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(quantity);
        order.setTotalAmount(product.getFinalPrice() * quantity);
        order.setOrderDate(java.time.LocalDateTime.now());
        order.setShippingAddress(address);
        order.setMobileNumber(mobile);

        orderRepo.save(order);

        return "redirect:/user/dashboard";
    }
}