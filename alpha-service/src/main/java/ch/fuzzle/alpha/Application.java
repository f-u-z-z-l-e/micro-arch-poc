package ch.fuzzle.alpha;

import ch.fuzzle.alpha.repository.InfoEntity;
import ch.fuzzle.alpha.repository.InfoRepository;
import java.util.Comparator;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@SpringBootApplication(scanBasePackages = "ch.fuzzle")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@Component
class DataLoader {

    private final InfoRepository repository;

    public DataLoader(InfoRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    private void load() {
        repository.deleteAll().thenMany(
                Flux.just("Info1", "Info2", "Info3", "Info4")
                        .map(name -> new InfoEntity(UUID.randomUUID().toString(), name))
                        .sort(Comparator.comparing(InfoEntity::getMessage))
                        .flatMap(repository::save))
                .thenMany(repository.findAll())
                .subscribe(System.out::println);
    }
}

