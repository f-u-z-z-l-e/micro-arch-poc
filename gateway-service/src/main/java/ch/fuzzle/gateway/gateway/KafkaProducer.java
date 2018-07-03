package ch.fuzzle.gateway.gateway;

import ch.fuzzle.event.account.AccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
public class KafkaProducer {

    private KafkaTemplate<String, AccountEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, AccountEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(AccountEvent accountEvent) {
        Assert.notNull(accountEvent, "Input cannot be null");
        log.info("Sending message: " + accountEvent.toString());
        ListenableFuture<SendResult<String, AccountEvent>> future = kafkaTemplate.sendDefault(accountEvent);

        String eventId = accountEvent.getEventId().toString();
        future.addCallback(result -> log.info("Successful: " + eventId), fail -> log.info("Failure: " + eventId));
    }

}
