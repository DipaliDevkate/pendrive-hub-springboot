package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;

    public void saveProduct(Product product) {
        productRepo.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(int id) {
        return productRepo.findById(id).orElse(null);
    }

    public void deleteProduct(int id) {
        productRepo.deleteById(id);
    }
}
