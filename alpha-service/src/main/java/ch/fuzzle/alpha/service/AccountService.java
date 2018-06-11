package ch.fuzzle.alpha.service;

import ch.fuzzle.model.AccountRequest;
import ch.fuzzle.model.BalanceRequest;
import ch.fuzzle.model.Person;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private Map<Person, AccountRequest> accountRepository = new HashMap<>();
    private Map<String, BigDecimal> balanceRepository = new HashMap<>();

    public AccountRequest findByName(String firstname, String lastname) {
        Optional<Person> result = accountRepository.keySet().stream()
                .filter(p -> firstname.equals(p.getFirstname()))
                .filter(p -> lastname.equals(p.getLastname()))
                .findAny();

        if (result.isPresent()) {
            return accountRepository.get(result.get());
        }

        return null;
    }

    public AccountRequest findByAccountId(String accountId) {
        String[] id = accountId.split("-");
        return findByName(id[0], id[1]);
    }


    public void addAccount(AccountRequest accountRequest) {
        this.accountRepository.put(accountRequest.getAccountHolder(), accountRequest);
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
