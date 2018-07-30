package ch.fuzzle.account.service;

import ch.fuzzle.account.stream.AccountInformation;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Service;

import static ch.fuzzle.account.stream.AccountBinding.ACCOUNT_INFORMATION;

@Service
public class AccountService {

    private final QueryableStoreRegistry registry;

    public AccountService(QueryableStoreRegistry registry) {
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
