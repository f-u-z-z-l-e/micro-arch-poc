package ch.fuzzle.alpha.repository;


import java.util.UUID;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableCassandraRepositories
public interface InfoRepository extends ReactiveCrudRepository<InfoEntity, String> {


}
