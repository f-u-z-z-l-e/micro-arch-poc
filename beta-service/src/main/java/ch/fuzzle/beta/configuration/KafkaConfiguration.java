package ch.fuzzle.beta.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaConfiguration {

//    @Value("${kafka.bootstrap-servers}")
//    private String bootstrapServer;
//
//    @Bean
//    public ProducerFactory<String, Info> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigs());
//    }
//
//    @Bean
//    public Map<String, Object> producerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return props;
//    }
//
//    @Bean
//    public KafkaTemplate<String, Info> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }

}
