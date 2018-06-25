package ch.fuzzle.account.configuration;

import ch.fuzzle.event.account.AccountEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import static ch.fuzzle.event.account.AccountEventType.ACCOUNT_CREATED;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamsConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<>(AccountEvent.class).getClass());
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, String.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, AccountEvent.class);
        return new StreamsConfig(props);
    }

    @Bean
    public KStream<String, AccountEvent> kStreamJson(StreamsBuilder builder) {
        KStream<String, AccountEvent> stream = builder.stream("account", Consumed.with(Serdes.String(), new JsonSerde<>(AccountEvent.class)));

        KTable<String, Long> combinedDocuments = stream
                .filter((key, value) -> value.getType() == ACCOUNT_CREATED)
                .map((key, value) -> new KeyValue<>(value.getAccountId(), value))
                .groupByKey()
                .count();

//        combinedDocuments.toStream().to("streams-json-output", Produced.with(Serdes.String(), new JsonSerde<>(AccountEvent.class)));
//        combinedDocuments.toStream().to("streams-json-output", Produced.with(Serdes.String(), Serdes.Long()));
        combinedDocuments.toStream().print(Printed.toSysOut());

        return stream;
    }

}