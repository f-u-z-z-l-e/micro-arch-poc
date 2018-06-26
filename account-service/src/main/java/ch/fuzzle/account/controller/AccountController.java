package ch.fuzzle.account.controller;

import ch.fuzzle.account.service.AccountService;
import ch.fuzzle.model.AccountRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("api/v1")
class AccountController {

    private AccountService service;

    public AccountController(AccountService accountService) {
        this.service = accountService;
    }


    @ApiOperation(value = "find by name", notes = "Find an existing account by firstname and lastname. ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @ResponseBody
    @GetMapping(value = "/account/{firstname}-{lastname}", produces = APPLICATION_JSON_UTF8_VALUE)
    public AccountRequest findByName(@PathVariable String firstname, @PathVariable String lastname) {
        AccountRequest accountRequest = service.findByName(firstname, lastname);
        if (accountRequest == null) {
            throw new ResourceNotFoundException();
        }

        return accountRequest;
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