package com.gym.service;

import com.gym.common.Result;
import com.gym.entity.Notice;
import com.gym.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    public Result<List<Notice>> findAll() {
        return Result.success(noticeMapper.findAll());
    }

    public Result<Notice> findById(Integer id) {
        return Result.success(noticeMapper.findById(id));
    }

    public Result<Void> save(Notice notice) {
        if (notice.getId() == null) {
            noticeMapper.insert(notice);
        } else {
            noticeMapper.update(notice);
        }
        return Result.success(null);
    }

    public Result<Void> delete(Integer id) {
        noticeMapper.delete(id);
        return Result.success(null);
    }
}

