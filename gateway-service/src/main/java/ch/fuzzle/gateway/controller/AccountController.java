package ch.fuzzle.gateway.controller;

import ch.fuzzle.gateway.service.AccountService;
import ch.fuzzle.model.AccountRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.util.StringUtils.isEmpty;

@RestController
@RequestMapping("api/v1")
public class AccountController {
    private static final String ERROR_MISSING_IDS = "Cannot create response uri, as eventId is missing!";

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "create account", notes = "Creates an account.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/account", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> createAccount(@RequestBody AccountRequest request) {
        return ResponseEntity.created(createResponseUriLocation(accountService.createAccount(request))).build();
    }

    protected URI createResponseUriLocation(UUID eventId) {
        if (isEmpty(eventId)) {
            throw new IllegalArgumentException(ERROR_MISSING_IDS);
        }

        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/../event/{event-id}")
                .buildAndExpand(eventId.toString())
                .normalize()
                .toUri();
    }


//    @GetMapping(value = "/account/{firstname}-{lastname}", produces = APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    @ResponseStatus
//    public AccountRequest findByName(@PathVariable(value = "firstname") String firstname, @PathVariable(value = "lastname") String lastname) {
//        return accountService.findByName(firstname, lastname);
//    }

}
