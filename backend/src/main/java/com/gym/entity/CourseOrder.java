package com.gym.entity;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class CourseOrder {
    private Integer id;
    
    @Size(max = 50, message = "订单号长度不能超过50个字符")
    private String orderNo;
    
    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Integer userId;
    
    @NotNull(message = "课程ID不能为空")
    @Min(value = 1, message = "课程ID必须大于0")
    private Integer courseId;
    
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    private BigDecimal price;
    
    @Min(value = 0, message = "状态必须为0、1或2")
    @Max(value = 2, message = "状态必须为0、1或2")
    private Integer status;
    
    private Date createTime;
    private Date updateTime;
    
    // 关联字段
    private String userName;
    private String courseName;
}

