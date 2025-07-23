package ru.practicum.yandex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients(basePackages = "ru.yandex.practicum")
public class ShoppingCartApp {
	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApp.class, args);
	}
}
