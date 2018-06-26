package ch.fuzzle.gateway.gateway;

import ch.fuzzle.model.AccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class AccountServiceRestClient {

    //    @Value("${rest.account-service.base-url}")
    private String baseUrl = "http://localhost:8081/api/v1";

    private final RestTemplate restTemplate;

    public AccountServiceRestClient(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }


    public AccountRequest findByName(String firstname, String lastname) {
        log.info("Requesting account for '{} {}' from account-service.", firstname, lastname);
        String url = getUriBuilder().path("/account/{firstname}-{lastname}").buildAndExpand(firstname, lastname).toUriString();

        try {
            return restTemplate.getForEntity(url, AccountRequest.class).getBody();
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                log.info("'{} {}' is not known by account-service.", firstname, lastname);
            } else {
                log.info("An error occurred accessing account-service!", firstname, lastname, e);
            }

            return null;
        }
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl);
    }
}