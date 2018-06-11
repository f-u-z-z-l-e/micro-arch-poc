package ch.fuzzle.alpha.gateway;

import ch.fuzzle.alpha.service.AccountService;
import ch.fuzzle.event.account.AccountEvent;
import ch.fuzzle.model.AccountRequest;
import ch.fuzzle.model.BalanceRequest;
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

        switch (accountEvent.getType()) {
            case ACCOUNT_CREATED:
                handleAccountCreatedEvent(accountEvent);
                break;
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

    private void handleAccountCreatedEvent(AccountEvent accountEvent) {
        ObjectReader objectReader = objectMapper.readerFor(AccountRequest.class);
        try {
            AccountRequest accountRequest = objectReader.readValue(accountEvent.getData());
            Person accountHolder = accountRequest.getAccountHolder();

            if (accountService.findByName(accountHolder.getFirstname(), accountHolder.getLastname()) == null) {
                accountService.addAccount(accountRequest);
            } else {
                log.info("Omit creating account for {} - {}, as one already exists", accountHolder.getFirstname(), accountHolder.getLastname());
            }

        } catch (IOException e) {
            log.error("an error occurred reading event data: ", e);
        }
    }

}