package ch.fuzzle.accountbalance.service;

import ch.fuzzle.accountbalance.gateway.KafkaProducer;
import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.model.BalanceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Service;

import static ch.fuzzle.accountbalance.stream.AccountBalanceBinding.ACCOUNT_BALANCE;
import static ch.fuzzle.event.account.AccountEventType.BALANCE_ADDED;
import static ch.fuzzle.event.account.AccountEventType.BALANCE_WITHDRAWN;
import static ch.fuzzle.model.BalanceOperation.ADD;

@Slf4j
@Service
public class AccountBalanceService {

    private final ObjectMapper objectMapper;
    private final AccountRegistrationService registrationService;
    private final KafkaProducer kafkaProducer;
    private final QueryableStoreRegistry registry;

    public AccountBalanceService(ObjectMapper objectMapper, AccountRegistrationService registrationService, KafkaProducer kafkaProducer, QueryableStoreRegistry registry) {
        this.objectMapper = objectMapper;
        this.registrationService = registrationService;
        this.kafkaProducer = kafkaProducer;
        this.registry = registry;
    }

    public BigDecimal getBalance(String accountId) {
        ReadOnlyKeyValueStore<String, AccountBalance> store = registry.getQueryableStoreType(ACCOUNT_BALANCE, QueryableStoreTypes.keyValueStore());
        AccountBalance accountBalance = store.get(accountId);

        return accountBalance == null ? new BigDecimal("0") : accountBalance.getBalance();
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
        if (!registrationService.accountExists(firstname, lastname)) {
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
