package ch.fuzzle.gateway.gateway;

import ch.fuzzle.model.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, Info> kafkaTemplate;

    @Value("${kafka.topics.info}")
    private String infoTopic;

    public void sendMessage(Info info) {
        Assert.notNull(info, "Input cannot be null");
        logger.info("Sending message: " + info.toString());
        ListenableFuture<SendResult<String, Info>> future = kafkaTemplate.send(infoTopic, info);
    }

}
