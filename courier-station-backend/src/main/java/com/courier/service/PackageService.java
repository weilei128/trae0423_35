package com.courier.service;

import com.courier.entity.Package;
import com.courier.mapper.PackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PackageService {
    @Autowired
    private PackageMapper packageMapper;

    public Package storePackage(String barcode, String recipientName, String recipientPhone, String courierName, String courierPhone) {
        Package pkg = new Package();
        pkg.setBarcode(barcode);
        pkg.setRecipientName(recipientName);
        pkg.setRecipientPhone(recipientPhone);
        pkg.setCourierName(courierName);
        pkg.setCourierPhone(courierPhone);
        pkg.setStorageTime(LocalDateTime.now());
        pkg.setStatus(0); // 待取件
        
        // 自动分配货架位
        pkg.setShelfPosition(generateShelfPosition());
        
        // 生成取件码
        pkg.setPickupCode(generatePickupCode());
        
        packageMapper.insert(pkg);
        
        // TODO: 发送通知
        
        return pkg;
    }

    private String generateShelfPosition() {
        // 简单的货架位生成逻辑：A-F区，1-5层，1-20号
        char area = (char) ('A' + new Random().nextInt(6));
        int level = 1 + new Random().nextInt(5);
        int position = 1 + new Random().nextInt(20);
        return area + level + "-" + position;
    }

    private String generatePickupCode() {
        // 生成6位数字取件码
        return String.format("%06d", new Random().nextInt(1000000));
    }

    public Package pickupPackage(String pickupCode) {
        // 根据取件码查询包裹
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Package> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("pickup_code", pickupCode);
        Package pkg = packageMapper.selectOne(wrapper);
        
        if (pkg == null) {
            return null; // 包裹不存在
        }
        
        if (pkg.getStatus() == 1) {
            throw new RuntimeException("包裹已被领取"); // 包裹已取件
        }
        
        if (pkg.getStatus() == 0) {
            pkg.setPickupTime(LocalDateTime.now());
            pkg.setStatus(1); // 已取件
            packageMapper.updateById(pkg);
        }
        
        return pkg;
    }

    public void checkOverduePackages() {
        // 检查超期包裹（入库超过7天）
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<Package> wrapper = new com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper<>();
        wrapper.eq("status", 0);
        wrapper.lt("storage_time", sevenDaysAgo);
        wrapper.set("status", 2); // 超期
        packageMapper.update(null, wrapper);
    }
}