package com.offertalk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.offertalk.mapper")
@EnableScheduling
@EnableAsync
public class OfferTalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(OfferTalkApplication.class, args);
    }
}
