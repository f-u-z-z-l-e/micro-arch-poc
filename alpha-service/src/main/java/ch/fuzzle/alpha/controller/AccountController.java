package ch.fuzzle.alpha.controller;

import ch.fuzzle.alpha.service.AccountService;
import ch.fuzzle.model.AccountRequest;
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

}

@ResponseStatus(value = NOT_FOUND, reason = "some reason")
class ResourceNotFoundException extends RuntimeException {

}