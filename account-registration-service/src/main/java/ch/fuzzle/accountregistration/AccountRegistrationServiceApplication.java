package ch.fuzzle.accountregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "ch.fuzzle")
public class AccountRegistrationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountRegistrationServiceApplication.class, args);
    }
}
