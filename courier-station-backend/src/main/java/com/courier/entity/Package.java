package com.courier.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@Data
@TableName("package")
public class Package {
    private Long id;
    private String barcode; // 快递条码
    private String pickupCode; // 取件码
    private String shelfPosition; // 货架位
    private String recipientName; // 收件人姓名
    private String recipientPhone; // 收件人电话
    private String courierName; // 快递员姓名
    private String courierPhone; // 快递员电话
    private LocalDateTime storageTime; // 入库时间
    private LocalDateTime pickupTime; // 取件时间
    private Integer status; // 状态：0-待取件，1-已取件，2-超期
    private String notes; // 备注
}