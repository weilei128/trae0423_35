package com.courier.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@Data
@TableName("charge")
public class Charge {
    private Long id;
    private String chargeNo; // 收费单号
    private Long packageId; // 包裹ID
    private Long sendOrderId; // 寄件订单ID
    private String type; // 收费类型：storage_fee（超期保管费）、send_fee（寄件费）
    private Double amount; // 金额
    private LocalDateTime createTime; // 创建时间
    private Integer status; // 状态：0-未支付，1-已支付
    private String notes; // 备注
}