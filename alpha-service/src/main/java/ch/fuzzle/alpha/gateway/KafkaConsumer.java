package ch.fuzzle.alpha.gateway;

import ch.fuzzle.model.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "${kafka.topics.info}", groupId = "consumer-group")
    public void listen(Info info) {
        logger.info("Received message: " + info);
    }
}