package com.courier.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@Data
@TableName("send_order")
public class SendOrder {
    private Long id;
    private String orderNo; // 寄件单号
    private String senderName; // 寄件人姓名
    private String senderPhone; // 寄件人电话
    private String senderAddress; // 寄件人地址
    private String recipientName; // 收件人姓名
    private String recipientPhone; // 收件人电话
    private String recipientAddress; // 收件人地址
    private Double weight; // 重量
    private Double price; // 寄件费用
    private String courierCompany; // 快递公司
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime pickupTime; // 揽收时间
    private Integer status; // 状态：0-待揽收，1-已揽收，2-已完成
    private String notes; // 备注
}