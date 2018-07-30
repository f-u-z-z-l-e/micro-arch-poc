package ch.fuzzle.accountbalance.stream;

import ch.fuzzle.event.account.AccountEvent;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface AccountBalanceBinding {

    String ACCOUNT_IN = "account-in";
    String ACCOUNT_BALANCE = "account-balance";

    @Input(ACCOUNT_IN)
    KStream<String, AccountEvent> accountEventsIn();

}
