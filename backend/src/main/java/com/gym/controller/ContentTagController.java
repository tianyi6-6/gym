package com.gym.controller;

import com.gym.entity.ContentTag;
import com.gym.service.ContentTagService;
import com.gym.common.Result;
import com.gym.common.RequireRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tag")
public class ContentTagController {

    @Autowired
    private ContentTagService tagService;

    @PostMapping("/create")
    @RequireRole({"coach", "admin"})
    public Result<String> createTag(@RequestBody ContentTag tag) {
        try {
            tagService.createTag(tag);
            return Result.success("标签创建成功");
        } catch (Exception e) {
            return Result.error("标签创建失败：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    @RequireRole({"coach", "admin"})
    public Result<String> updateTag(@RequestBody ContentTag tag) {
        try {
            tagService.updateTag(tag);
            return Result.success("标签更新成功");
        } catch (Exception e) {
            return Result.error("标签更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @RequireRole({"coach", "admin"})
    public Result<String> deleteTag(@PathVariable Integer id) {
        try {
            tagService.deleteTag(id);
            return Result.success("标签删除成功");
        } catch (Exception e) {
            return Result.error("标签删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    @RequireRole({"coach", "admin", "user"})
    public Result<ContentTag> getTagById(@PathVariable Integer id) {
        try {
            ContentTag tag = tagService.getTagById(id);
            return Result.success(tag);
        } catch (Exception e) {
            return Result.error("获取标签失败：" + e.getMessage());
        }
    }

    @GetMapping("/get-by-name/{name}")
    @RequireRole({"coach", "admin", "user"})
    public Result<ContentTag> getTagByName(@PathVariable String name) {
        try {
            ContentTag tag = tagService.getTagByName(name);
            return Result.success(tag);
        } catch (Exception e) {
            return Result.error("获取标签失败：" + e.getMessage());
        }
    }

    @GetMapping("/all")
    @RequireRole({"coach", "admin", "user"})
    public Result<List<ContentTag>> getAllTags() {
        try {
            List<ContentTag> tags = tagService.getAllTags();
            return Result.success(tags);
        } catch (Exception e) {
            return Result.error("获取标签列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/by-content/{contentId}")
    @RequireRole({"coach", "admin", "user"})
    public Result<List<ContentTag>> getTagsByContentId(@PathVariable Integer contentId) {
        try {
            List<ContentTag> tags = tagService.getTagsByContentId(contentId);
            return Result.success(tags);
        } catch (Exception e) {
            return Result.error("获取标签列表失败：" + e.getMessage());
        }
    }
}
