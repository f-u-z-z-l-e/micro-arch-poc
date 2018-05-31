package ch.fuzzle.gateway.service;

import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.gateway.gateway.KafkaProducer;
import ch.fuzzle.model.AccountRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static ch.fuzzle.event.account.AccountEventType.ACCOUNT_CREATED;

@Slf4j
@Service
public class AccountService {

    private KafkaProducer kafkaProducer;
    private ObjectMapper objectMapper;

    public AccountService(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = new ObjectMapper();
    }

    public UUID createAccount(AccountRequest request) {
        UUID eventId = UUID.randomUUID();

        AccountEvent accountEvent = new AccountEvent();
        accountEvent.setType(ACCOUNT_CREATED);
        accountEvent.setEventId(eventId);
        accountEvent.setTimestamp(ZonedDateTime.now().toString());
        accountEvent.setData(convert(request));

        kafkaProducer.sendMessage(accountEvent);

        return eventId;
    }

    private String convert(AccountRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert AccountRequest!", e);
        }

        return "";
    }
}
