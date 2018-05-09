package ch.fuzzle.gateway.scheduler;

import ch.fuzzle.gateway.gateway.KafkaProducer;
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
        Info info = Info.Builder.info(ZonedDateTime.now().toString(), "scheduled message");

        logger.info(info.toString());
        kafkaProducer.sendMessage(info);
    }

}
