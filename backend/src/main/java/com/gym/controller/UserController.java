package com.gym.controller;

import com.gym.common.Result;
import com.gym.entity.User;
import com.gym.mapper.UserMapper;
import com.gym.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;
    


    // 上传文件保存路径
    private static final String UPLOAD_PATH = System.getProperty("user.dir") + "/upload/gym/user/";
    // 最大文件大小限制（2MB）
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    // 允许的文件类型
    private static final String[] ALLOWED_EXTENSIONS = { "jpg", "jpeg", "png" };

    @PostMapping("/register")
    public Result<Void> register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/list")
    public Result<List<User>> list() {
        return userService.findAll();
    }

    /**
     * 上传头像
     */
    @PostMapping("/upload-avatar")
    @Transactional
    public Result<String> uploadAvatar(@RequestParam("username") String username,
            @RequestParam("avatar") MultipartFile file) {
        try {
            // 直接从请求参数中获取用户名
            User user = userMapper.findByUsername(username);

            if (user == null) {
                return Result.error("用户不存在");
            }

            // 验证文件大小
            if (file.getSize() > MAX_FILE_SIZE) {
                return Result.error("文件大小超过限制，最大允许2MB");
            }

            // 验证文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !isAllowedExtension(originalFilename)) {
                return Result.error("不支持的文件类型，仅支持JPG、PNG格式");
            }

            // 确保上传目录存在
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    return Result.error("创建上传目录失败");
                }
            }

            // 生成唯一文件名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + suffix;

            // 保存文件
            File dest = new File(UPLOAD_PATH + filename);
            file.transferTo(dest);

            // 更新头像路径
            String avatarUrl = "/upload/gym/user/" + filename;
            user.setAvatar(avatarUrl);
            int result = userMapper.update(user);

            if (result > 0) {
                return Result.success("头像上传成功", avatarUrl);
            } else {
                // 事务回滚，删除已上传的文件
                if (dest.exists()) {
                    dest.delete();
                }
                return Result.error("头像上传失败");
            }
        } catch (IOException e) {
            return Result.error("头像上传失败：" + e.getMessage());
        } catch (Exception e) {
            return Result.error("头像上传失败");
        }
    }

    /**
     * 检查文件扩展名是否允许
     */
    private boolean isAllowedExtension(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        for (String allowedExtension : ALLOWED_EXTENSIONS) {
            if (extension.equals("." + allowedExtension)) {
                return true;
            }
        }
        return false;
    }
}

