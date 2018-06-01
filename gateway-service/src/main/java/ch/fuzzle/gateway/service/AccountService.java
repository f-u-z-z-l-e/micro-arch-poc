package ch.fuzzle.gateway.service;

import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.gateway.gateway.AccountServiceRestClient;
import ch.fuzzle.gateway.gateway.KafkaProducer;
import ch.fuzzle.model.AccountRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import static ch.fuzzle.event.account.AccountEventType.ACCOUNT_CREATED;

@Slf4j
@Service
public class AccountService {

    private KafkaProducer kafkaProducer;
    private ObjectMapper objectMapper;
    private ValidationService validationService;
    private AccountServiceRestClient accountServiceRestClient;

    public AccountService(KafkaProducer kafkaProducer, ObjectMapper objectMapper, ValidationService validationService, AccountServiceRestClient accountServiceRestClient) {
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
        this.validationService = validationService;
        this.accountServiceRestClient = accountServiceRestClient;
    }

    public UUID createAccount(AccountRequest request) {
        UUID eventId = UUID.randomUUID();
        String firstname = request.getAccountHolder().getFirstname();
        String lastname = request.getAccountHolder().getLastname();

        // validate request
        if (!validationService.accountExists(firstname, lastname)) {
            // prepare event
            AccountEvent accountEvent = new AccountEvent();
            accountEvent.setType(ACCOUNT_CREATED);
            accountEvent.setEventId(eventId);
            accountEvent.setTimestamp(ZonedDateTime.now().toString());
            accountEvent.setData(convert(request));

            // broadcast accountEvent
            kafkaProducer.sendMessage(accountEvent);

        }

        // return uuid to caller
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

    public AccountRequest findByName(String firstname, String lastname) {
        try {
            return accountServiceRestClient.findByName(firstname, lastname);
        } catch (HttpClientErrorException e) {
            log.info("Error occurred while trying to find account for {} - {}!", firstname, lastname, e);
            return null;
        }

    }
}
