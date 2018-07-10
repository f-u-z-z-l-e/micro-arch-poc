package ch.fuzzle.account.stream;

import ch.fuzzle.model.AccountRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOverview {

    private AccountRequest request;
    private Status status;

    enum Status {
        ENABLED, DISABLED
    }
}

