import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class AvroConsumer {

    private final KafkaConsumer<String, GenericRecord> consumer;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ApiClient apiClient;

    public AvroConsumer(String bootstrapServers,
                        String schemaRegistryUrl,
                        String topic,
                        String groupId,
                        String destinationUrl) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put("specific.avro.reader", false); // false = GenericRecord, true = SpecificRecord

        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Collections.singletonList(topic));
        this.apiClient = new ApiClient(destinationUrl);

    }

    /**
     * Starts polling messages and sending them to handlers.
     */
    public void start() {
        try {
            while (running.get()) {
                ConsumerRecords<String, GenericRecord> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, GenericRecord> record : records) {
                    ApiResult apiResult = apiClient.send(record.toString());
                    System.out.println("Consumed record" + record.key() + "and called api with api result, successful:"+ apiResult.isSuccess());
                }
                consumer.commitAsync();
            }
        } finally {
            shutdown();
        }
    }


    public void shutdown() {
        running.set(false);
        try {
            consumer.wakeup();
        } catch (Exception ignored) {}
        finally {
            consumer.close();
        }
    }

    public static void main(String[] args) {
        String bootstrapServers = "localhost:29092";
        String schemaRegistryUrl = "http://localhost:8081";
        String topic = "user";
        String groupId = "avro-consumer-group-2";

        AvroConsumer consumer = new AvroConsumer(
                bootstrapServers, schemaRegistryUrl, topic, groupId,"google.com"
        );

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::shutdown));
        consumer.start();
    }
}
