package ch.fuzzle.gateway.kafka;

import ch.fuzzle.model.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, Info> kafkaTemplate;

    public void sendMessage(Info info) {
        Assert.notNull(info, "Input cannot be null");
        logger.info("Sending message: " + info);
        kafkaTemplate.send("foo", info);
    }
}
