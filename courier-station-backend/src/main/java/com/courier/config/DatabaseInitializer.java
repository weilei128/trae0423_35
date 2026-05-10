package com.courier.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseInitializer {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DataSource dataSource;

    @Bean
    public ApplicationRunner initializeDatabase() {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                // 执行数据库初始化脚本
                Resource resource = resourceLoader.getResource("classpath:init.sql");
                ScriptUtils.executeSqlScript(connection, resource);
                System.out.println("Database initialization completed successfully!");
            } catch (SQLException e) {
                System.err.println("Error initializing database: " + e.getMessage());
            }
        };
    }
}