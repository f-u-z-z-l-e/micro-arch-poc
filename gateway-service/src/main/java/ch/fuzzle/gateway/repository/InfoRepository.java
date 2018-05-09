package ch.fuzzle.gateway.repository;

import ch.fuzzle.model.Info;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class InfoRepository {

    public Set<Info> infoDb = new HashSet<>();

    public Flux<Info> findById(String id) {
        return Flux.fromStream(infoDb.stream()
                .filter(Objects::nonNull)
                .filter(i -> i.getId().equals(id)));
    }

    public void insert(Mono<Info> infoMono) {
        infoDb.add(infoMono.block());
    }
}
