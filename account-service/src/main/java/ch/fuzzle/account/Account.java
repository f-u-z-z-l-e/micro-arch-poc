package ch.fuzzle.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ch.fuzzle")
public class Account {
    public static void main(String[] args) {
        SpringApplication.run(Account.class, args);
    }
}
