package ch.fuzzle.accountregistration.service;

import ch.fuzzle.accountregistration.stream.AccountInformation;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Service;

import static ch.fuzzle.accountregistration.stream.AccountBinding.ACCOUNT_INFORMATION;

@Service
public class AccountRegistrationService {

    private final QueryableStoreRegistry registry;

    public AccountRegistrationService(QueryableStoreRegistry registry) {
        this.registry = registry;
    }

    public AccountInformation findByName(String firstname, String lastname) {
        ReadOnlyKeyValueStore<String, AccountInformation> store = registry.getQueryableStoreType(ACCOUNT_INFORMATION, QueryableStoreTypes.keyValueStore());

        return store.get(firstname + "-" + lastname);
    }

    public AccountInformation findByAccountId(String accountId) {
        String[] id = accountId.split("-");
        return findByName(id[0], id[1]);
    }

}