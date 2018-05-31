package ch.fuzzle.gateway.gateway;

import ch.fuzzle.event.account.AccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, AccountEvent> kafkaTemplate;

    @Value("${kafka.topics.account}")
    private String accountTopic;

    public void sendMessage(AccountEvent accountEvent) {
        Assert.notNull(accountEvent, "Input cannot be null");
        log.info("Sending message: " + accountEvent.toString());
        ListenableFuture<SendResult<String, AccountEvent>> future = kafkaTemplate.send(accountTopic, accountEvent);
    }

}
