package ch.fuzzle.accountregistration.stream;

import ch.fuzzle.model.AccountRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountInformation {

    private AccountRequest request;
    private Status status;

    public enum Status {
        ENABLED, DISABLED
    }
}

