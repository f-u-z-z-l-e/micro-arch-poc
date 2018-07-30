package ch.fuzzle.gateway.gateway;

import ch.fuzzle.model.AccountRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class AccountServiceRestClient {

    @Autowired
    private DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    public AccountServiceRestClient(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }


    public AccountRequest findByName(String firstname, String lastname) {
        log.info("Requesting account for '{} {}' from account-registration-service.", firstname, lastname);
        String url = getUriBuilder().path("api/v1/account/{firstname}-{lastname}").buildAndExpand(firstname, lastname).toUriString();

        try {
            return restTemplate.getForEntity(url, AccountRequest.class).getBody();
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                log.info("'{} {}' is not known by account-registration-service.", firstname, lastname);
            } else {
                log.info("An error occurred accessing account-service!", firstname, lastname, e);
            }

            return null;
        }
    }

    private UriComponentsBuilder getUriBuilder() {
        List<ServiceInstance> instances = discoveryClient.getInstances("account-registration-service");

        if (instances != null) {
            log.info("Instances found: " + instances.size());
        }

        return UriComponentsBuilder.fromHttpUrl(instances.get(0).getUri().toASCIIString());
    }

}
