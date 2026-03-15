package com.gym.entity;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Recharge {
    private Integer id;
    
    @NotNull(message = "用户ID不能为空")
    @Min(value = 1, message = "用户ID必须大于0")
    private Integer userId;
    
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;
    
    @NotNull(message = "类型不能为空")
    @Min(value = 1, message = "类型必须为1或2")
    @Max(value = 2, message = "类型必须为1或2")
    private Integer type;
    
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;
    
    private Date createTime;
    
    // 关联字段
    private String userName;
}

