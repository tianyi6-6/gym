package com.gym.controller;

import com.gym.entity.FitnessContent;
import com.gym.service.FitnessContentService;
import com.gym.common.Result;
import com.gym.common.RequireRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/content")
public class FitnessContentController {

    @Autowired
    private FitnessContentService contentService;

    @PostMapping("/create")
    @RequireRole({"coach", "admin"})
    public Result<String> createContent(@RequestBody Map<String, Object> params) {
        try {
            FitnessContent content = new FitnessContent();
            content.setTitle((String) params.get("title"));
            content.setContent((String) params.get("content"));
            content.setSummary((String) params.get("summary"));
            content.setCoverImage((String) params.get("coverImage"));
            content.setAuthorId((Integer) params.get("authorId"));
            content.setAuthorName((String) params.get("authorName"));
            content.setStatus((Integer) params.get("status"));
            Object tagIdsObj = params.get("tagIds");
            List<Integer> tagIds = new java.util.ArrayList<>();
            if (tagIdsObj instanceof List<?>) {
                for (Object item : (List<?>) tagIdsObj) {
                    if (item instanceof Integer) {
                        tagIds.add((Integer) item);
                    }
                }
            }
            contentService.createContent(content, tagIds);
            return Result.success("内容创建成功");
        } catch (Exception e) {
            return Result.error("内容创建失败：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    @RequireRole({"coach", "admin"})
    public Result<String> updateContent(@RequestBody Map<String, Object> params) {
        try {
            FitnessContent content = new FitnessContent();
            content.setId((Integer) params.get("id"));
            content.setTitle((String) params.get("title"));
            content.setContent((String) params.get("content"));
            content.setSummary((String) params.get("summary"));
            content.setCoverImage((String) params.get("coverImage"));
            content.setAuthorId((Integer) params.get("authorId"));
            content.setAuthorName((String) params.get("authorName"));
            content.setStatus((Integer) params.get("status"));
            Object tagIdsObj = params.get("tagIds");
            List<Integer> tagIds = new java.util.ArrayList<>();
            if (tagIdsObj instanceof List<?>) {
                for (Object item : (List<?>) tagIdsObj) {
                    if (item instanceof Integer) {
                        tagIds.add((Integer) item);
                    }
                }
            }
            contentService.updateContent(content, tagIds);
            return Result.success("内容更新成功");
        } catch (Exception e) {
            return Result.error("内容更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @RequireRole({"coach", "admin"})
    public Result<String> deleteContent(@PathVariable Integer id) {
        try {
            contentService.deleteContent(id);
            return Result.success("内容删除成功");
        } catch (Exception e) {
            return Result.error("内容删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    @RequireRole({"coach", "admin", "user"})
    public Result<FitnessContent> getContentById(@PathVariable Integer id) {
        try {
            FitnessContent content = contentService.getContentById(id);
            contentService.incrementViewCount(id);
            return Result.success(content);
        } catch (Exception e) {
            return Result.error("获取内容失败：" + e.getMessage());
        }
    }

    @GetMapping("/by-author/{authorId}")
    @RequireRole({"coach", "admin"})
    public Result<List<FitnessContent>> getContentsByAuthorId(@PathVariable Integer authorId) {
        try {
            List<FitnessContent> contents = contentService.getContentsByAuthorId(authorId);
            return Result.success(contents);
        } catch (Exception e) {
            return Result.error("获取内容列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/by-status/{status}")
    @RequireRole({"coach", "admin"})
    public Result<List<FitnessContent>> getContentsByStatus(@PathVariable Integer status) {
        try {
            List<FitnessContent> contents = contentService.getContentsByStatus(status);
            return Result.success(contents);
        } catch (Exception e) {
            return Result.error("获取内容列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/by-tag/{tagId}")
    @RequireRole({"coach", "admin", "user"})
    public Result<List<FitnessContent>> getContentsByTagId(@PathVariable Integer tagId) {
        try {
            List<FitnessContent> contents = contentService.getContentsByTagId(tagId);
            return Result.success(contents);
        } catch (Exception e) {
            return Result.error("获取内容列表失败：" + e.getMessage());
        }
    }

    @PostMapping("/search")
    @RequireRole({"coach", "admin", "user"})
    public Result<List<FitnessContent>> searchContents(@RequestBody Map<String, Object> conditions) {
        try {
            List<FitnessContent> contents = contentService.getContentsByConditions(conditions);
            return Result.success(contents);
        } catch (Exception e) {
            return Result.error("搜索内容失败：" + e.getMessage());
        }
    }

    @GetMapping("/hot")
    @RequireRole({"coach", "admin", "user"})
    public Result<List<FitnessContent>> getHotContents(@RequestParam Integer limit) {
        try {
            List<FitnessContent> contents = contentService.getHotContents(limit);
            return Result.success(contents);
        } catch (Exception e) {
            return Result.error("获取热门内容失败：" + e.getMessage());
        }
    }

    @PostMapping("/like/{id}")
    @RequireRole({"coach", "admin", "user"})
    public Result<String> likeContent(@PathVariable Integer id) {
        try {
            contentService.incrementLikeCount(id);
            return Result.success("点赞成功");
        } catch (Exception e) {
            return Result.error("点赞失败：" + e.getMessage());
        }
    }

    @PostMapping("/collect/{id}")
    @RequireRole({"coach", "admin", "user"})
    public Result<String> collectContent(@PathVariable Integer id) {
        try {
            contentService.incrementCollectCount(id);
            return Result.success("收藏成功");
        } catch (Exception e) {
            return Result.error("收藏失败：" + e.getMessage());
        }
    }

    @PostMapping("/share/{id}")
    @RequireRole({"coach", "admin", "user"})
    public Result<String> shareContent(@PathVariable Integer id) {
        try {
            contentService.incrementShareCount(id);
            return Result.success("分享成功");
        } catch (Exception e) {
            return Result.error("分享失败：" + e.getMessage());
        }
    }
}
