package com.gym.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Recharge {
    private Integer id;
    private Integer userId;
    private BigDecimal amount;
    private Integer type;
    private String remark;
    private Date createTime;
    
    // 关联字段
    private String userName;
}

