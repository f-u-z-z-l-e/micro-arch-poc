package ch.fuzzle.accountregistration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FetchConfig {

    @Value("${store.properties.foo}")
    String password;

    @GetMapping("/property")
    public ResponseEntity<String> getProperty() {
        return ResponseEntity.ok(password);
    }
}
