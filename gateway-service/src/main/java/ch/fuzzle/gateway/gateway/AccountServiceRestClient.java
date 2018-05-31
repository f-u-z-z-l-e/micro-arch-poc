package ch.fuzzle.gateway.gateway;

import ch.fuzzle.model.AccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
        String url = getUriBuilder().path("/account/{firstname}-{lastname}").buildAndExpand(firstname, lastname).toUriString();

        if (log.isDebugEnabled()) {
            log.debug("Requesting account for = '{}' - '{}'", firstname, lastname);
        }

        ResponseEntity<AccountRequest> response = restTemplate.getForEntity(url, AccountRequest.class);
        return response.getBody();
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl);
    }
}
