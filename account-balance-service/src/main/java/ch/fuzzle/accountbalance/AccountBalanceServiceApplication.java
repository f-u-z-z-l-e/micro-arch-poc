package ch.fuzzle.accountbalance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "ch.fuzzle")
public class AccountBalanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountBalanceServiceApplication.class, args);
    }
}

