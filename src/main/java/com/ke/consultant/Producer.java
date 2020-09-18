package com.ke.consultant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * Query Student database for all information and publish to topic channel
 */
public class Producer {
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Student";
    private static final String KAFKA_BROKER = "localhost:9092";
    private static Connection connection;
    private static Statement statement;

    public static void main(String[] args) throws Exception {
        connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("Select * from Student");

        ObjectMapper mapper = new ObjectMapper();

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        ProducerRecord<String, String> producerRecord;
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        StudentBean bean = new StudentBean();
        while (resultSet.next()) {
            bean.setId(resultSet.getInt("id"));
            bean.setFirst(resultSet.getString("firstname"));
            bean.setLast(resultSet.getString("lastname"));
            bean.setMail(resultSet.getString("email"));
            String value = mapper.writeValueAsString(bean);
            producerRecord = new ProducerRecord<>("channel", "name", value);
            kafkaProducer.send(producerRecord);
        }
        kafkaProducer.close();

    }


}
