package com.drotsakura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SpringBootReggieApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootReggieApplication.class, args);
    }

}
