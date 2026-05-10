package com.courier;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.courier.mapper")
public class CourierStationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourierStationApplication.class, args);
    }
}
