package ch.fuzzle.accountregistration.stream;

import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.model.AccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
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
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;

import static ch.fuzzle.accountregistration.stream.AccountBinding.ACCOUNT_IN;
import static ch.fuzzle.accountregistration.stream.AccountBinding.ACCOUNT_INFORMATION;
import static ch.fuzzle.accountregistration.stream.AccountInformation.Status.DISABLED;
import static ch.fuzzle.accountregistration.stream.AccountInformation.Status.ENABLED;
import static ch.fuzzle.event.account.AccountEventType.ACCOUNT_CREATED;
import static ch.fuzzle.event.account.AccountEventType.ACCOUNT_REACTIVATED;

@Slf4j
@Component
@EnableBinding(AccountBinding.class)
public class AccountRegistrationSink {

    private ObjectMapper objectMapper;

    public AccountRegistrationSink(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @StreamListener
    public void process(@Input(ACCOUNT_IN) KStream<String, AccountEvent> events) {
        processAccountInformation(events);
    }


    private void processAccountInformation(KStream<String, AccountEvent> events) {
        events
            .filter(AccountRegistrationSink::accountModificationEvent)
            .map((key, value) -> new KeyValue<>(value.getAccountId(), value))
            .groupByKey(Serialized.with(Serdes.String(), new JsonSerde<>(AccountEvent.class)))
            .aggregate(() -> new AccountInformation(), new AccountInformationAggregator(),
                Materialized.<String, AccountEvent, KeyValueStore<Bytes, byte[]>>as(ACCOUNT_INFORMATION)
                    .withKeySerde(Serdes.String()).withValueSerde(new JsonSerde(AccountInformation.class)));
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
