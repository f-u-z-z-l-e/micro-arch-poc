package ch.fuzzle.accountbalance.controller;

import ch.fuzzle.accountbalance.service.AccountBalanceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@RestController
@RequestMapping("api/v1")
public class AccountBalanceController {

    private AccountBalanceService service;

    public AccountBalanceController(AccountBalanceService service) {
        this.service = service;
    }

    @ApiOperation(value = "get balance", notes = "Return the balance of the requested account. ")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    @ResponseBody
    @GetMapping(value = "/balance/{firstname}-{lastname}", produces = APPLICATION_JSON_UTF8_VALUE)
    public BigDecimal getBalance(@PathVariable String firstname, @PathVariable String lastname) {
        BigDecimal balance = service.getBalance(firstname + "-" + lastname);
        if (balance == null) {
            throw new ResourceNotFoundException();
        }

        return balance;
    }
}

@ResponseStatus(value = NOT_FOUND, reason = "The requested resource could not be found.")
class ResourceNotFoundException extends RuntimeException {

}