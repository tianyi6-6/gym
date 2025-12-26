package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.Notice;
import com.gym.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@CrossOrigin
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("/list")
    public Result<List<Notice>> list() {
        return noticeService.findAll();
    }

    @GetMapping("/{id}")
    public Result<Notice> getById(@PathVariable Integer id) {
        return noticeService.findById(id);
    }

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Notice notice) {
        return noticeService.save(notice);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        return noticeService.delete(id);
    }
}

