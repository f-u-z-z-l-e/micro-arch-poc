package ch.fuzzle.gateway.service;

import ch.fuzzle.gateway.gateway.AccountServiceRestClient;
import ch.fuzzle.model.AccountRequest;
import ch.fuzzle.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
public class ValidationService {

    private AccountServiceRestClient accountServiceRestClient;

    public ValidationService(AccountServiceRestClient accountServiceRestClient) {
        this.accountServiceRestClient = accountServiceRestClient;
    }

    public boolean accountExists(String firstname, String lastname) {
        AccountRequest accountRequest;
        try {
            accountRequest = accountServiceRestClient.findByName(firstname, lastname);
        } catch (HttpClientErrorException e) {
            return false;
        }

        Person accountHolder = accountRequest.getAccountHolder();

        return firstname.equals(accountHolder.getFirstname()) && lastname.equals(accountHolder.getLastname());
    }
}
