package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.service.AdminService;
import com.example.demo.service.OrderService;
import com.example.demo.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    // Admin login page
    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin_login";
    }

    // Handle admin login
    @PostMapping("/login")
    public String adminLogin(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session) {
        Admin admin = adminService.login(email, password);
        if (admin != null) {
            session.setAttribute("admin", admin);
            return "redirect:/admin/dashboard";
        }
        return "redirect:/admin/login?error";
    }

    // Admin dashboard: products + all orders
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Product> products = productService.getAllProducts();
        List<Order> allOrders = orderService.getAllOrders();

        model.addAttribute("products", products);
        model.addAttribute("orders", allOrders);

        return "admin_dashboard";
    }

    // Add product page
    @GetMapping("/product/add")
    public String addProductPage() {
        return "add_product";
    }

    // Add product
    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/images/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            product.setImagePath("/images/" + file.getOriginalFilename());
        }

        product.setFinalPrice(product.getPrice() - product.getDiscount());
        productService.saveProduct(product);
        return "redirect:/admin/dashboard";
    }

    // Edit product page
    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "edit_product";
    }

    // Update product
    @PostMapping("/product/update")
    public String updateProduct(@ModelAttribute Product product,
                                @RequestParam("imageFile") MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            String uploadDir = System.getProperty("user.dir") + "/uploads/images/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            product.setImagePath("/images/" + file.getOriginalFilename());
        }

        product.setFinalPrice(product.getPrice() - product.getDiscount());
        productService.saveProduct(product);
        return "redirect:/admin/dashboard";
    }

    // Delete product
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "redirect:/admin/dashboard";
    }

    // View all orders only (optional page)
    @GetMapping("/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin_orders";
    }
}