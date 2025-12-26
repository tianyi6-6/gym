package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.CourseOrder;
import com.gym.service.CourseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class CourseOrderController {
    @Autowired
    private CourseOrderService orderService;

    @GetMapping("/list")
    public Result<List<CourseOrder>> list() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public Result<CourseOrder> getById(@PathVariable Integer id) {
        return orderService.findById(id);
    }

    @GetMapping("/user/{userId}")
    public Result<List<CourseOrder>> getByUserId(@PathVariable Integer userId) {
        return orderService.findByUserId(userId);
    }

    @PostMapping("/create")
    public Result<Void> createOrder(@RequestBody CourseOrder order) {
        return orderService.createOrder(order);
    }

    @PostMapping("/update")
    public Result<Void> updateOrder(@RequestBody CourseOrder order) {
        return orderService.updateOrder(order);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteOrder(@PathVariable Integer id) {
        return orderService.deleteOrder(id);
    }

    @PostMapping("/cancel/{id}")
    public Result<Void> cancelOrder(@PathVariable Integer id) {
        return orderService.cancelOrder(id);
    }

    @PostMapping("/pay/{id}")
    public Result<Void> payOrder(@PathVariable Integer id) {
        return orderService.payOrder(id);
    }
}

