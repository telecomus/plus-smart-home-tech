package ru.yandex.practicum.commerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"ru.yandex.practicum.interaction_api.interaction"})
@SpringBootApplication
public class Order {
    public static void main(String[] args) {
        SpringApplication.run(Order.class, args);
    }
}
