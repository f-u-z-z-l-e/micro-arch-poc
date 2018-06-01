package ch.fuzzle.gateway.service;

import ch.fuzzle.gateway.gateway.AccountServiceRestClient;
import ch.fuzzle.model.AccountRequest;
import ch.fuzzle.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidationService {

    private AccountServiceRestClient accountServiceRestClient;

    public ValidationService(AccountServiceRestClient accountServiceRestClient) {
        this.accountServiceRestClient = accountServiceRestClient;
    }

    public boolean accountExists(String firstname, String lastname) {
        AccountRequest accountRequest = accountServiceRestClient.findByName(firstname, lastname);
        if (accountRequest == null) {
            return false;
        }

        Person accountHolder = accountRequest.getAccountHolder();

        if (firstname.equals(accountHolder.getFirstname()) && lastname.equals(accountHolder.getLastname())) {
            log.info("An account for {} - {} already exists!", firstname, lastname);
            return true;
        }

        log.info("Account identifier mismatch for {} - {} found!", firstname, lastname);
        return false;
    }
}
