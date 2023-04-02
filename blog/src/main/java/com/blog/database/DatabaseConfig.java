package com.blog.database;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public static DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(String.format("jdbc:mysql://google/%s?cloudSqlInstance=%s", "blog", "zenith-blog-pipe:us-central1:blog-database"));
        config.setUsername("blog-connect");
        config.setPassword("N|/R(,XD4%fa;ck~");
        config.setMaximumPoolSize(5);
        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
        config.addDataSourceProperty("cloudSqlInstance", "zenith-blog-pipe:us-central1:blog-database");

        return new HikariDataSource(config);
    }
}