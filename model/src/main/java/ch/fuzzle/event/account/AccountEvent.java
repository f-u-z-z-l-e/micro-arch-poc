package ch.fuzzle.event.account;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEvent {

    private String accountId;

    private UUID eventId;

    private String timestamp;

    private AccountEventType type;

    private String data;

}
