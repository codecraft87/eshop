package io.github.codecraft87.eshop.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EshopOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EshopOrderServiceApplication.class, args);
	}

}
