package ch.fuzzle.gateway.kafka;

import ch.fuzzle.model.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {
    @Autowired
    KafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 50000)
    public void sendMessage() {
        String message = "My message is cool !";
        Info info = new Info();
        info.setId("1");
        info.setMesage("from scheduler");
        kafkaProducer.sendMessage(info);
    }
}
