package ch.fuzzle.account.controller;

import ch.fuzzle.account.service.AccountService;
import ch.fuzzle.model.AccountRequest;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("api/v1")
class AccountController {

    private AccountService service;

    public AccountController(AccountService accountService) {
        this.service = accountService;
    }

    @ResponseBody
    @GetMapping(value = "/account/{firstname}-{lastname}", produces = APPLICATION_JSON_UTF8_VALUE)
    public AccountRequest findByName(@PathVariable String firstname, @PathVariable String lastname) {
        AccountRequest accountRequest = service.findByName(firstname, lastname);
        if (accountRequest == null) {
            throw new ResourceNotFoundException();
        }

        return accountRequest;
    }

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