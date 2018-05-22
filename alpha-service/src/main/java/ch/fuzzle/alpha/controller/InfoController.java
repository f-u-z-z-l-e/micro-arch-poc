package ch.fuzzle.alpha.controller;

import ch.fuzzle.alpha.repository.InfoEntity;
import ch.fuzzle.alpha.repository.InfoRepository;
import java.time.Duration;
import java.util.Comparator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
class InfoController {
    private final InfoRepository repository;

    InfoController(InfoRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/infoD", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<InfoEntity> getAllInfoDelayed() {
        return repository.findAll().delayElements(Duration.ofSeconds(1));
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Flux<InfoEntity> getAllInfo() {
        return repository.findAll().sort(Comparator.comparing(InfoEntity::getMessage));
    }
}
