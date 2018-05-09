package ch.fuzzle.gateway.kafka;

import ch.fuzzle.gateway.repository.InfoRepository;
import ch.fuzzle.model.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private InfoRepository infoRepository;

    @KafkaListener(topics = "foo", groupId = "consumer-group")
    public void listen(Info message) {
        logger.info("Received message: " + message);
        infoRepository.insert(Mono.just(message));
    }
}
