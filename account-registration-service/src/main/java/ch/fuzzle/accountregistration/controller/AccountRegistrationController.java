package ch.fuzzle.accountregistration.controller;

import ch.fuzzle.accountregistration.service.AccountRegistrationService;
import ch.fuzzle.accountregistration.stream.AccountInformation;
import ch.fuzzle.model.AccountRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static ch.fuzzle.accountregistration.stream.AccountInformation.Status.DISABLED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("api/v1")
class AccountController {
    private static final String ERROR_MISSING_IDS = "Cannot create response uri, as eventId is missing!";

    private AccountRegistrationService service;

    public AccountController(AccountRegistrationService accountRegistrationService) {
        this.service = accountRegistrationService;
    }

    @ApiOperation(value = "create account", notes = "Creates an account.")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/account", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> createAccount(@RequestBody AccountRequest request) {
        return ResponseEntity.created(createResponseUriLocation(service.createAccount(request))).build();
    }

    protected URI createResponseUriLocation(UUID eventId) {
        if (StringUtils.isEmpty(eventId)) {
            throw new IllegalArgumentException(ERROR_MISSING_IDS);
        }

        return ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/../event/{event-id}")
            .buildAndExpand(eventId.toString())
            .normalize()
            .toUri();
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