package ch.fuzzle.accountbalance.service;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalance {

    private BigDecimal balance = new BigDecimal(0);
}
