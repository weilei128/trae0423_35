package com.courier.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notification {
    private Long id;
    private Long packageId; // 包裹ID
    private String recipientPhone; // 收件人电话
    private String content; // 通知内容
    private LocalDateTime sendTime; // 发送时间
    private Integer status; // 状态：0-未发送，1-已发送
    private String type; // 通知类型：package_arrived（包裹到站）、overdue_reminder（超期提醒）
}