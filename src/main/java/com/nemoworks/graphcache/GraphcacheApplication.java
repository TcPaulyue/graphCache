package com.nemoworks.graphcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class GraphcacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphcacheApplication.class, args);
    }

    @Bean
    Connection PostgresConnection(){
        Connection c = null;
        try {
            c = DriverManager
                    .getConnection("jdbc:postgresql://127.0.0.1:5432/postgres",
                            "postgres", "123456");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return c;
    }

}
