package ch.fuzzle.account.stream;

import ch.fuzzle.event.account.AccountEvent;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface AccountBinding {

    String ACCOUNT_IN = "account-in";
    String ACCOUNT_OVERVIEW = "account-overview-out";

    @Input(ACCOUNT_IN)
    KStream<String, AccountEvent> accountEventsIn();

//    @Output(ACCOUNT_OVERVIEW)
//    KTable<String, AccountRequest> lalala();


}