# Kafka Avro Example

This project demonstrates a simple flow of producing and consuming messages with **Kafka** using **Avro** serialization and a **Schema Registry**.

---

## Prerequisites

- Docker & Docker Compose installed
- Maven or Gradle build tool

---

## Steps to Run

### 1. Start Kafka, Zookeeper, and Schema Registry

Use the provided `docker-compose.yml` to bring up the required services:

```bash
docker-compose up -d
```

This will start:
- **Zookeeper**
- **Kafka Broker**
- **Confluent Schema Registry**

---

### 2. Register a Dummy Schema in Schema Registry

Run the `scripts.sh` file to create and register a schema:

```bash
./scripts.sh
```

This will:
- Post an Avro schema to Schema Registry
- Make the schema available for producer/consumer applications

---

### 3. Produce a Message with AvroProducer

Run the `AvroProducer` main class to send a message to Kafka:


The producer will:
- Serialize the message using Avro
- Use the registered schema from Schema Registry
- Send the message to the configured Kafka topic

---

### 4. Consume a Message with AvroConsumer

Run the `AvroConsumer` main class to consume the message and call an external API:

The consumer will:
- Deserialize the message using Avro
- Validate against the schema from Schema Registry
- Process the message and invoke the configured API endpoint

---

## Cleanup

To stop all running containers:

```bash
docker-compose down
```

---

## Project Structure

```
.
├── docker-compose.yml         # Kafka, Zookeeper, Schema Registry services
├── scripts.sh                 # Registers a dummy schema
├── src/main/java/com/example/kafka/
│   ├── AvroProducer.java      # Producer implementation
│   └── AvroConsumer.java      # Consumer implementation
└── README.md                  # Project documentation
```
