package com.courier.service;

import com.courier.entity.Package;
import com.courier.mapper.PackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {
    @Autowired
    private PackageMapper packageMapper;

    public Map<String, Object> getDailyReport(LocalDate date) {
        Map<String, Object> report = new HashMap<>();
        
        // 计算当天的开始和结束时间
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        
        // 日收件量
        int dailyStorageCount = getPackageCount(startOfDay, endOfDay, null);
        report.put("dailyStorageCount", dailyStorageCount);
        
        // 日取件量
        int dailyPickupCount = getPackageCount(startOfDay, endOfDay, 1);
        report.put("dailyPickupCount", dailyPickupCount);
        
        // 取件率
        double pickupRate = dailyStorageCount > 0 ? (double) dailyPickupCount / dailyStorageCount * 100 : 0;
        report.put("pickupRate", pickupRate);
        
        // 滞留件数量（状态为0且入库超过3天）
        int overdueCount = getOverduePackageCount(3);
        report.put("overdueCount", overdueCount);
        
        // 总包裹数
        int totalPackageCount = getTotalPackageCount();
        report.put("totalPackageCount", totalPackageCount);
        
        // 总取件数
        int totalPickupCount = getTotalPickupCount();
        report.put("totalPickupCount", totalPickupCount);
        
        return report;
    }

    private int getPackageCount(LocalDateTime start, LocalDateTime end, Integer status) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Package> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.ge("storage_time", start);
        wrapper.lt("storage_time", end);
        if (status != null) {
            wrapper.eq("status", status);
        }
        return packageMapper.selectCount(wrapper).intValue();
    }

    private int getOverduePackageCount(int days) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(days);
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Package> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("status", 0);
        wrapper.lt("storage_time", cutoffTime);
        return packageMapper.selectCount(wrapper).intValue();
    }

    private int getTotalPackageCount() {
        return packageMapper.selectCount(null).intValue();
    }

    private int getTotalPickupCount() {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Package> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("status", 1);
        return packageMapper.selectCount(wrapper).intValue();
    }
}