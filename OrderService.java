package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;

    public void placeOrder(Order order) {
        orderRepo.save(order);
    }

    public List<Order> getUserOrders(User user) {
        return orderRepo.findByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
}
