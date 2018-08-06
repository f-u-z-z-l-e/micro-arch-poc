package ch.fuzzle.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableDiscoveryClient // TODO: Remove, should be activate if jar in classpath
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "ch.fuzzle")
public class GatewayServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}

