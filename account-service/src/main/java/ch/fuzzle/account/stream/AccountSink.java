package ch.fuzzle.account.stream;

import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.event.account.AccountEventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import static ch.fuzzle.account.stream.AccountBinding.ACCOUNT_IN;

@Slf4j
@Component
@EnableBinding(AccountBinding.class)
public class AccountSink {

    @StreamListener(ACCOUNT_IN)
//    public void process(@Input(ACCOUNT_IN) KStream<String, AccountEvent> events) {
    public void process(KStream<String, AccountEvent> events) {
        events
            .filter((key, value) -> AccountEventType.ACCOUNT_CREATED == value.getType())
            .map((key, value) -> new KeyValue<>(value.getAccountId(), value))
            .print(Printed.toSysOut());

    }
}
