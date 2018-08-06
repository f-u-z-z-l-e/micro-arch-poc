package ch.fuzzle.accountregistration.service;

import ch.fuzzle.accountregistration.gateway.KafkaProducer;
import ch.fuzzle.accountregistration.stream.AccountInformation;
import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.model.AccountRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Service;

import static ch.fuzzle.accountregistration.stream.AccountBinding.ACCOUNT_INFORMATION;
import static ch.fuzzle.event.account.AccountEventType.ACCOUNT_CREATED;

@Service
@Slf4j
public class AccountRegistrationService {

    private final QueryableStoreRegistry registry;
    private ObjectMapper objectMapper;
    private KafkaProducer kafkaProducer;

    public AccountRegistrationService(QueryableStoreRegistry registry, ObjectMapper objectMapper, KafkaProducer kafkaProducer) {
        this.registry = registry;
        this.objectMapper = objectMapper;
        this.kafkaProducer = kafkaProducer;
    }

    public AccountInformation findByName(String firstname, String lastname) {
        ReadOnlyKeyValueStore<String, AccountInformation> store = registry.getQueryableStoreType(ACCOUNT_INFORMATION, QueryableStoreTypes.keyValueStore());

        AccountInformation accountInformation = store.get(firstname + "-" + lastname);
        return accountInformation;
    }

    public UUID createAccount(AccountRequest request) {
        UUID eventId = UUID.randomUUID();
        String firstname = request.getAccountHolder().getFirstname();
        String lastname = request.getAccountHolder().getLastname();

        // validate request
        if (findByName(firstname, lastname) == null) {
            // prepare event
            AccountEvent accountEvent = new AccountEvent();
            accountEvent.setType(ACCOUNT_CREATED);
            accountEvent.setEventId(eventId);
            accountEvent.setTimestamp(ZonedDateTime.now().toString());
            accountEvent.setAccountId(firstname + "-" + lastname);
            accountEvent.setData(convert(request));

            // broadcast accountEvent
            kafkaProducer.sendMessage(accountEvent);
        }

        // return uuid to caller
        return eventId;
    }

    private String convert(Object request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("Unable to convert AccountRequest!", e);
        }

        return "";
    }

}
