package com.courier.controller;

import com.courier.entity.Package;
import com.courier.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/package")
public class PackageController {
    @Autowired
    private PackageService packageService;

    @PostMapping("/store")
    public Package storePackage(@RequestParam String barcode, 
                              @RequestParam String recipientName, 
                              @RequestParam String recipientPhone, 
                              @RequestParam String courierName, 
                              @RequestParam String courierPhone) {
        return packageService.storePackage(barcode, recipientName, recipientPhone, courierName, courierPhone);
    }

    @PostMapping("/pickup")
    public Object pickupPackage(@RequestParam String pickupCode) {
        try {
            Package pkg = packageService.pickupPackage(pickupCode);
            if (pkg == null) {
                return new java.util.HashMap<String, Object>() {{
                    put("code", 404);
                    put("message", "包裹不存在");
                }};
            }
            return pkg;
        } catch (RuntimeException e) {
            return new java.util.HashMap<String, Object>() {{
                put("code", 400);
                put("message", e.getMessage());
            }};
        }
    }

    @PostMapping("/check-overdue")
    public void checkOverduePackages() {
        packageService.checkOverduePackages();
    }
}