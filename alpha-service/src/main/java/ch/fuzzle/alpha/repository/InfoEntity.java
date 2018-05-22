package ch.fuzzle.alpha.repository;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("info")
@AllArgsConstructor
@NoArgsConstructor
public class InfoEntity {

    @PrimaryKey()
    private String id;
    private String message;

}
