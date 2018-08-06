package ch.fuzzle.accountbalance.controller;

import ch.fuzzle.accountbalance.service.AccountBalanceService;
import ch.fuzzle.model.BalanceRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
@RestController
@RequestMapping("api/v1")
public class AccountBalanceController {
    private static final String ERROR_MISSING_IDS = "Cannot create response uri, as eventId is missing!";

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

    @ApiOperation(value = "modify balance of an account", notes = "Increase or decreases balance of existing account.")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/balance/{firstname}-{lastname}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> addBalance(@PathVariable(value = "firstname") String firstname,
                                           @PathVariable(value = "lastname") String lastname,
                                           @RequestBody BalanceRequest request) {

        UUID eventId = service.modifyBalance(firstname, lastname, request);

        if (eventId == null) {
            throw new ResourceNotFoundException();
        }

        return ResponseEntity.created(createResponseUriLocation(eventId)).build();
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
}

@ResponseStatus(value = NOT_FOUND, reason = "The requested resource could not be found.")
class ResourceNotFoundException extends RuntimeException {

}