package ch.fuzzle.account.service;

import ch.fuzzle.account.stream.AccountOverview;
import ch.fuzzle.model.BalanceRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Service;

import static ch.fuzzle.account.stream.AccountBinding.ACCOUNT_OVERVIEW;

@Service
public class AccountService {

    private final QueryableStoreRegistry registry;
    private Map<String, BigDecimal> balanceRepository = new HashMap<>();

    public AccountService(QueryableStoreRegistry registry) {
        this.registry = registry;
    }

    public AccountOverview findByName(String firstname, String lastname) {
        ReadOnlyKeyValueStore<String, AccountOverview> store = registry.getQueryableStoreType(ACCOUNT_OVERVIEW, QueryableStoreTypes.keyValueStore());
        KeyValueIterator<String, AccountOverview> all = store.all();

        return store.get(firstname + "-" + lastname);
    }

    public AccountOverview findByAccountId(String accountId) {
        String[] id = accountId.split("-");
        return findByName(id[0], id[1]);
    }

    public BigDecimal getBalance(String accountId) {
        return balanceRepository.get(accountId);
    }

    public void addBalance(String accountId, BalanceRequest balanceRequest) {
        BigDecimal previousAmount = this.balanceRepository.get(accountId);

        if (previousAmount == null) {
            this.balanceRepository.put(accountId, new BigDecimal(balanceRequest.getAmount()));
        } else {
            BigDecimal newAmount = previousAmount.add(new BigDecimal(balanceRequest.getAmount()));
            this.balanceRepository.put(accountId, newAmount);
        }

    }

    public void withdrawBalance(String accountId, BalanceRequest balanceRequest) {
        BigDecimal previousAmount = this.balanceRepository.get(accountId);

        if (previousAmount == null) {
            this.balanceRepository.put(accountId, new BigDecimal(balanceRequest.getAmount()).multiply(new BigDecimal("-1")));
        } else {
            BigDecimal newAmount = previousAmount.subtract(new BigDecimal(balanceRequest.getAmount()));
            this.balanceRepository.put(accountId, newAmount);
        }

    }
}
