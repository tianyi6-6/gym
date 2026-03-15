package com.gym.controller;

import com.gym.entity.Admin;
import com.gym.mapper.AdminMapper;
import com.gym.common.Result;
import com.gym.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private JWTUtil jwtUtil;

    // 上传文件保存路径
    private static final String UPLOAD_PATH = System.getProperty("user.dir") + "/upload/gym/admin/";
    // 最大文件大小限制（2MB）
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    // 允许的文件类型
    private static final String[] ALLOWED_EXTENSIONS = { "jpg", "jpeg", "png" };

    /**
     * 获取管理员信息
     */
    @GetMapping("/info")
    public Result<Admin> getAdminInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // 从token中获取用户名，若没有token则默认使用admin
            String username = "admin";
            if (token != null && token.startsWith("Bearer ")) {
                username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            }
            Admin admin = adminMapper.findByUsername(username);
            if (admin != null) {
                // 清除密码字段
                admin.setPassword(null);
                return Result.success("获取管理员信息成功", admin);
            } else {
                return Result.error("管理员不存在");
            }
        } catch (Exception e) {
            return Result.error("获取管理员信息失败");
        }
    }

    /**
     * 更新管理员信息
     */
    @PostMapping("/update")
    public Result<String> updateAdminInfo(@RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody Admin admin) {
        try {
            // 从token中获取用户名，若没有token则默认使用admin
            String username = "admin";
            if (token != null && token.startsWith("Bearer ")) {
                username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            }
            Admin currentAdmin = adminMapper.findByUsername(username);

            if (currentAdmin != null) {
                // 只更新允许修改的字段
                currentAdmin.setName(admin.getName());
                currentAdmin.setPhone(admin.getPhone());
                currentAdmin.setEmail(admin.getEmail());

                int result = adminMapper.update(currentAdmin);
                if (result > 0) {
                    return Result.success("更新管理员信息成功");
                } else {
                    return Result.error("更新管理员信息失败");
                }
            } else {
                return Result.error("管理员不存在");
            }
        } catch (Exception e) {
            return Result.error("更新管理员信息失败");
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ChangePasswordRequest request) {
        try {
            // 从token中获取用户名，若没有token则默认使用admin
            String username = "admin";
            if (token != null && token.startsWith("Bearer ")) {
                username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            }
            Admin admin = adminMapper.findByUsername(username);

            if (admin != null) {
                // 验证原密码
                if (!admin.getPassword().equals(request.getOldPassword())) {
                    return Result.error("原密码错误");
                }

                // 更新密码
                admin.setPassword(request.getNewPassword());
                int result = adminMapper.update(admin);
                if (result > 0) {
                    return Result.success("密码修改成功");
                } else {
                    return Result.error("密码修改失败");
                }
            } else {
                return Result.error("管理员不存在");
            }
        } catch (Exception e) {
            return Result.error("密码修改失败");
        }
    }

    /**
     * 上传头像
     */
    @PostMapping("/upload-avatar")
    @Transactional
    public Result<String> uploadAvatar(@RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam("avatar") MultipartFile file) {
        try {
            // 从token中获取用户名，若没有token则默认使用admin
            String username = "admin";
            if (token != null && token.startsWith("Bearer ")) {
                username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            }
            Admin admin = adminMapper.findByUsername(username);

            if (admin == null) {
                return Result.error("管理员不存在");
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
            String avatarUrl = "/upload/gym/admin/" + filename;
            admin.setAvatar(avatarUrl);
            int result = adminMapper.update(admin);

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

    // 密码修改请求类
    static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
