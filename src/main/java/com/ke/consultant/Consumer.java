package com.ke.consultant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * Consume messages from topic "channel" and convert json back to Bean
 */
public class Consumer {

    public void consumer() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Producer.KAFKA_BROKER);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);
        kafkaConsumer.subscribe(Arrays.asList("student"));

        ObjectMapper mapper = new ObjectMapper();

        JDBC jdbc = new JDBC();
        while (true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMinutes(1));
            consumerRecords.forEach(consumerRecord -> {
                try {
                    StudentBean[] studentBeans = mapper.readValue(consumerRecord.value(), StudentBean[].class);
                    for (StudentBean studentBean : studentBeans) {
                        System.out.println(studentBean.toString());
                        jdbc.editDB(studentBean.getId(), studentBean.getFirst(), studentBean.getLast(), studentBean.getMail(), "student_replicate");
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("JSON error");
                }
            });
        }
    }
}
