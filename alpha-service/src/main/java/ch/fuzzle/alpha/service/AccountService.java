package ch.fuzzle.alpha.service;

import ch.fuzzle.model.AccountRequest;
import ch.fuzzle.model.Person;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private Map<Person, AccountRequest> repository = new HashMap<>();

    public AccountRequest findByName(String firstname, String lastname) {
        Optional<Person> result = repository.keySet().stream()
                .filter(p -> firstname.equals(p.getFirstname()))
                .filter(p -> lastname.equals(p.getLastname()))
                .findAny();

        if (result.isPresent()) {
            return repository.get(result.get());
        }

        return null;
    }

    public void add(AccountRequest accountRequest) {
        this.repository.put(accountRequest.getAccountHolder(), accountRequest);
    }
}
