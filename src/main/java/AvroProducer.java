import org.apache.kafka.clients.producer.RecordMetadata;
import org.kafka.core.client.impl.KafkaProducerClient;
import org.kafka.core.config.KafkaConfiguration;

import java.util.List;

public class AvroProducer {

    public static void main(String[] args) throws Exception {

        KafkaConfiguration kafkaConfiguration = new KafkaConfiguration();

        KafkaConfiguration.ProducerConfig config = new KafkaConfiguration.ProducerConfig();
        config.setTopic("user");
        config.setBrokers("localhost:29092");
        config.setAckRequired("all");
        config.setSchemaVersion(1);

        kafkaConfiguration.setProducerConfig(List.of(config));
        kafkaConfiguration.setSchemaRegistryConfig(new KafkaConfiguration.SchemaRegistryConfig("http://localhost:8081"));
        KafkaProducerClient kafkaProducerClient = new KafkaProducerClient(kafkaConfiguration);
        kafkaProducerClient.start();
        User user = new User(1l,"Ankur");
        RecordMetadata recordMetadata = kafkaProducerClient.sendMessageSync("user",""+user.getId(),user);
        System.out.println("Produced messsage with offset" + recordMetadata.offset());

    }

    public static class User {
        private Long id;
        private String name;

        public User(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public User(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
