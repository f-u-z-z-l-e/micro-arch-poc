package ch.fuzzle.accountbalance.service;

import ch.fuzzle.accountbalance.gateway.AccountRegistrationClient;
import ch.fuzzle.model.AccountRequest;
import ch.fuzzle.model.Person;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountRegistrationService {

    private AccountRegistrationClient registrationClient;

    public AccountRegistrationService(AccountRegistrationClient registrationClient) {
        this.registrationClient = registrationClient;
    }

    public boolean accountExists(String firstname, String lastname) {
        AccountRequest accountRequest;
        try {
            accountRequest = registrationClient.findByName(firstname, lastname);
        } catch (FeignException ex) {
            if (HttpStatus.NOT_FOUND.value() == ex.status()) {
                return false;
            } else {
                throw ex;
            }
        }

        Person accountHolder = accountRequest.getAccountHolder();

        if (firstname.equals(accountHolder.getFirstname()) && lastname.equals(accountHolder.getLastname())) {
            log.info("An account for {} - {} exists.", firstname, lastname);
            return true;
        }

        log.info("Account identifier mismatch for {} - {} found!", firstname, lastname);
        return false;
    }
}
