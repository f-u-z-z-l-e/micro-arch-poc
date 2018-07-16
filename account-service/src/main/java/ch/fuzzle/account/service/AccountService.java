package ch.fuzzle.account.service;

import ch.fuzzle.account.stream.AccountBalance;
import ch.fuzzle.account.stream.AccountInformation;
import java.math.BigDecimal;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Service;

import static ch.fuzzle.account.stream.AccountBinding.ACCOUNT_BALANCE;
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

    public BigDecimal getBalance(String accountId) {
        ReadOnlyKeyValueStore<String, AccountBalance> store = registry.getQueryableStoreType(ACCOUNT_BALANCE, QueryableStoreTypes.keyValueStore());
        AccountBalance accountBalance = store.get(accountId);

        return accountBalance == null ? new BigDecimal("0") : accountBalance.getBalance();
    }

}
