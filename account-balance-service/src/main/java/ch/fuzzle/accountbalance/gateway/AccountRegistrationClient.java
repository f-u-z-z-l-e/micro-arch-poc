package ch.fuzzle.accountbalance.gateway;

import ch.fuzzle.model.AccountRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@FeignClient(value = "account-registration-service")
public interface AccountRegistrationClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/account/{firstname}-{lastname}", produces = APPLICATION_JSON_UTF8_VALUE)
    AccountRequest findByName(@PathVariable String firstname, @PathVariable String lastname);

}
