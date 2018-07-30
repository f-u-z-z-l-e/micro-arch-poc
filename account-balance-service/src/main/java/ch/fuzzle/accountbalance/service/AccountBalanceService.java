package ch.fuzzle.accountbalance.service;

import java.math.BigDecimal;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Service;

import static ch.fuzzle.accountbalance.stream.AccountBalanceBinding.ACCOUNT_BALANCE;

@Service
public class AccountBalanceService {

    private final QueryableStoreRegistry registry;

    public AccountBalanceService(QueryableStoreRegistry registry) {
        this.registry = registry;
    }

    public BigDecimal getBalance(String accountId) {
        ReadOnlyKeyValueStore<String, AccountBalance> store = registry.getQueryableStoreType(ACCOUNT_BALANCE, QueryableStoreTypes.keyValueStore());
        AccountBalance accountBalance = store.get(accountId);

        return accountBalance == null ? new BigDecimal("0") : accountBalance.getBalance();
    }
}
