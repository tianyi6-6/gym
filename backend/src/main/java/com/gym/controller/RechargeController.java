package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.Recharge;
import com.gym.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/recharge")
@CrossOrigin
public class RechargeController {
    @Autowired
    private RechargeService rechargeService;

    @GetMapping("/user/{userId}")
    public Result<List<Recharge>> getByUserId(@PathVariable Integer userId) {
        return rechargeService.findByUserId(userId);
    }

    @PostMapping
    public Result<Void> recharge(@RequestBody @Valid Recharge recharge) {
        return rechargeService.recharge(recharge);
    }
}
