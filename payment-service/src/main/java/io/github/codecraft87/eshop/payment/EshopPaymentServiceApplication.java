package io.github.codecraft87.eshop.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EshopPaymentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EshopPaymentServiceApplication.class, args);
  }
}
