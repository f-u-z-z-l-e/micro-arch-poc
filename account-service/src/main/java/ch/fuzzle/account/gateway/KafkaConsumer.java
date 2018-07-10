package ch.fuzzle.account.gateway;

import ch.fuzzle.account.service.AccountService;
import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.model.BalanceRequest;
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


    @KafkaListener(topics = "${kafka.topics.account}")
    public void listen(AccountEvent accountEvent) {
        log.info("Received message: " + accountEvent);

        switch (accountEvent.getType()) {
            case BALANCE_ADDED:
                handleBalanceAddedEvent(accountEvent);
                break;
            case BALANCE_WITHDRAWN:
                handleBalanceWithdrawnEvent(accountEvent);
                break;
            default:
                log.info("Unhandled event: {}", accountEvent.toString());
        }

    }

    private void handleBalanceAddedEvent(AccountEvent accountEvent) {
        ObjectReader objectReader = objectMapper.readerFor(BalanceRequest.class);

        try {

            BalanceRequest balanceRequest = objectReader.readValue(accountEvent.getData());

            if (accountService.findByAccountId(accountEvent.getAccountId()) != null) {
                accountService.addBalance(accountEvent.getAccountId(), balanceRequest);
            }

        } catch (IOException e) {
            log.error("an error occurred reading event data: ", e);
        }

    }

    private void handleBalanceWithdrawnEvent(AccountEvent accountEvent) {
        ObjectReader objectReader = objectMapper.readerFor(BalanceRequest.class);

        try {

            BalanceRequest balanceRequest = objectReader.readValue(accountEvent.getData());

            if (accountService.findByAccountId(accountEvent.getAccountId()) != null) {
                accountService.withdrawBalance(accountEvent.getAccountId(), balanceRequest);
            }

        } catch (IOException e) {
            log.error("an error occurred reading event data: ", e);
        }

    }

}