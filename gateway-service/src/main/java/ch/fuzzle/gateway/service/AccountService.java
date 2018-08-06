package ch.fuzzle.gateway.service;

import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.gateway.gateway.AccountRegistrationClient;
import ch.fuzzle.gateway.gateway.KafkaProducer;
import ch.fuzzle.model.BalanceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static ch.fuzzle.event.account.AccountEventType.BALANCE_ADDED;
import static ch.fuzzle.event.account.AccountEventType.BALANCE_WITHDRAWN;
import static ch.fuzzle.model.BalanceOperation.ADD;

@Slf4j
@Service
public class AccountService {

    private KafkaProducer kafkaProducer;
    private ObjectMapper objectMapper;
    private ValidationService validationService;
    private AccountRegistrationClient accountRegistrationClient;

    public AccountService(KafkaProducer kafkaProducer, ObjectMapper objectMapper, ValidationService validationService, AccountRegistrationClient accountRegistrationClient) {
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
        this.validationService = validationService;
        this.accountRegistrationClient = accountRegistrationClient;
    }

    private String convert(Object request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert AccountRequest!", e);
        }

        return "";
    }

    public UUID modifyBalance(String firstname, String lastname, BalanceRequest request) {
        UUID eventId = UUID.randomUUID();

        // validate request
        if (!validationService.accountExists(firstname, lastname)) {
            return null;
        }

        // prepare event
        AccountEvent accountEvent = new AccountEvent();
        accountEvent.setType(request.getOperation() == ADD ? BALANCE_ADDED : BALANCE_WITHDRAWN);
        accountEvent.setEventId(eventId);
        accountEvent.setTimestamp(ZonedDateTime.now().toString());
        accountEvent.setAccountId(firstname + "-" + lastname);
        accountEvent.setData(convert(request));

        // broadcast accountEvent
        kafkaProducer.sendMessage(accountEvent);

        return eventId;
    }
}
