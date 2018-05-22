package ch.fuzzle.beta.scheduler;

import ch.fuzzle.beta.gateway.KafkaProducer;
import ch.fuzzle.model.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class MessagePublisher {
    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    @Autowired
    KafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 5000)
    public void sendMessage() {
        Info info = new Info(ZonedDateTime.now().toString(), "scheduled message");

        logger.info(info.toString());
        kafkaProducer.sendMessage(info);
    }

}
