package edu.pro.spbbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class SpbBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpbBaseApplication.class, args);
    }

}
