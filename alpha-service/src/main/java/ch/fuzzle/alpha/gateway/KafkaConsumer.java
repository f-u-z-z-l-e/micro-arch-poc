package ch.fuzzle.alpha.gateway;

import ch.fuzzle.alpha.service.AccountService;
import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.event.account.AccountEventType;
import ch.fuzzle.model.AccountRequest;
import ch.fuzzle.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    private ObjectMapper objectMapper;
    private AccountService accountService;

    public KafkaConsumer(ObjectMapper objectMapper, AccountService accountService) {
        this.objectMapper = objectMapper;
        this.accountService = accountService;
    }


    @KafkaListener(topics = "${kafka.topics.account}", groupId = "consumer-group")
    public void listen(AccountEvent accountEvent) {
        log.info("Received message: " + accountEvent);

        if (AccountEventType.ACCOUNT_CREATED == accountEvent.getType()) {
            ObjectReader objectReader = objectMapper.readerFor(AccountRequest.class);
            try {
                AccountRequest accountRequest = objectReader.readValue(accountEvent.getData());
                Person accountHolder = accountRequest.getAccountHolder();

                if (accountService.findByName(accountHolder.getFirstname(), accountHolder.getLastname()) == null) {
                    accountService.add(accountRequest);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}