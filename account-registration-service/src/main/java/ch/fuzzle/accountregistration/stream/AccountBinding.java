package ch.fuzzle.accountregistration.stream;

import ch.fuzzle.event.account.AccountEvent;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface AccountBinding {

    String ACCOUNT_IN = "account-in";
    String ACCOUNT_INFORMATION = "account-information";

    @Input(ACCOUNT_IN)
    KStream<String, AccountEvent> accountEventsIn();

}
