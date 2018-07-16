package ch.fuzzle.account.stream;

import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.model.AccountRequest;
import ch.fuzzle.model.BalanceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;

import static ch.fuzzle.account.stream.AccountBinding.*;
import static ch.fuzzle.account.stream.AccountInformation.Status.DISABLED;
import static ch.fuzzle.account.stream.AccountInformation.Status.ENABLED;
import static ch.fuzzle.event.account.AccountEventType.ACCOUNT_CREATED;
import static ch.fuzzle.event.account.AccountEventType.ACCOUNT_REACTIVATED;

@Slf4j
@Component
@EnableBinding(AccountBinding.class)
public class AccountSink {

    private ObjectMapper objectMapper;
    private QueryableStoreRegistry registry;

    public AccountSink(ObjectMapper objectMapper, QueryableStoreRegistry registry) {
        this.objectMapper = objectMapper;
        this.registry = registry;
    }

    @StreamListener
    public void process(@Input(ACCOUNT_IN) KStream<String, AccountEvent> events) {
        processAccountInformation(events);
        processAccountBalance(events);
    }

    private void processAccountBalance(KStream<String, AccountEvent> events) {
        events.
            filter(AccountSink::accountBalanceModificationEvent)
            .map((key, value) -> new KeyValue<>(value.getAccountId(), value))
            .groupByKey(Serialized.with(Serdes.String(), new JsonSerde<>(AccountEvent.class)))
            .aggregate(() -> new AccountBalance(), new AccountBalanceAggregator(),
                Materialized.<String, AccountEvent, KeyValueStore<Bytes, byte[]>>as(ACCOUNT_BALANCE)
                    .withKeySerde(Serdes.String()).withValueSerde(new JsonSerde(AccountBalance.class)));
    }

    private void processAccountInformation(KStream<String, AccountEvent> events) {
        events
            .filter(AccountSink::accountModificationEvent)
            .map((key, value) -> new KeyValue<>(value.getAccountId(), value))
            .groupByKey(Serialized.with(Serdes.String(), new JsonSerde<>(AccountEvent.class)))
            .aggregate(() -> new AccountInformation(), new AccountInformationAggregator(),
                Materialized.<String, AccountEvent, KeyValueStore<Bytes, byte[]>>as(ACCOUNT_INFORMATION)
                    .withKeySerde(Serdes.String()).withValueSerde(new JsonSerde(AccountInformation.class)));
    }

    public static boolean accountBalanceModificationEvent(String key, AccountEvent accountEvent) {
        switch (accountEvent.getType()) {
            case BALANCE_ADDED:
            case BALANCE_WITHDRAWN:
                return true;
            default:
                return false;
        }
    }

    public static boolean accountModificationEvent(String key, AccountEvent accountEvent) {
        switch (accountEvent.getType()) {
            case ACCOUNT_CREATED:
            case ACCOUNT_CLOSED:
            case ACCOUNT_SUSPENDED:
            case ACCOUNT_REACTIVATED:
                return true;
            default:
                return false;
        }
    }

    class AccountBalanceAggregator implements Aggregator<String, AccountEvent, AccountBalance> {
        @Override
        public AccountBalance apply(String key, AccountEvent event, AccountBalance aggregate) {
            BalanceRequest request = null;
            ObjectReader objectReader = objectMapper.readerFor(BalanceRequest.class);
            try {
                request = objectReader.readValue(event.getData());
            } catch (IOException e) {
                log.error("Unable to deserialize balanceRequest.", e);
            }

            if (request.getOperation() == null) {
                throw new IllegalStateException("null balance modification operation not allowed here!");
            }

            AccountBalance balance = new AccountBalance();

            switch (request.getOperation()) {
                case ADD:
                    balance.setBalance(aggregate.getBalance().add(new BigDecimal(request.getAmount())));
                    break;
                case WITHDRAW:
                    balance.setBalance(aggregate.getBalance().subtract(new BigDecimal(request.getAmount())));
                    break;
                default:
                    throw new IllegalStateException("unknown balance modification operation: " + request.getOperation());
            }

            return balance;
        }
    }

    class AccountInformationAggregator implements Aggregator<String, AccountEvent, AccountInformation> {
        @Override
        public AccountInformation apply(String key, AccountEvent event, AccountInformation aggregate) {
            AccountRequest request = null;
            ObjectReader objectReader = objectMapper.readerFor(AccountRequest.class);
            try {
                request = objectReader.readValue(event.getData());
            } catch (IOException e) {
                log.error("Unable to deserialize accountRequest.", e);
            }

            AccountInformation.Status status = asList(ACCOUNT_CREATED, ACCOUNT_REACTIVATED).contains(event.getType()) ? ENABLED : DISABLED;
            return new AccountInformation(request, status);
        }
    }

}
