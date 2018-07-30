package ch.fuzzle.accountregistration.controller;

import ch.fuzzle.accountregistration.service.AccountService;
import ch.fuzzle.accountregistration.stream.AccountInformation;
import ch.fuzzle.model.AccountRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static ch.fuzzle.accountregistration.stream.AccountInformation.Status.DISABLED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
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
        AccountInformation accountInformation = service.findByName(firstname, lastname);

        if (accountInformation == null || DISABLED == accountInformation.getStatus()) {
            throw new ResourceNotFoundException();
        }

        return accountInformation.getRequest();
    }

}

@ResponseStatus(value = NOT_FOUND, reason = "The requested resource could not be found.")
class ResourceNotFoundException extends RuntimeException {

}