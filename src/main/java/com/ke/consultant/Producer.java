package com.ke.consultant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

/**
 * Query Student database for all information and publish to topic channel
 */
public class Producer {
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Student";
    public static final String KAFKA_BROKER = "localhost:9092";

    public void producer() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, "root", "root");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("Select * from Student");) {

            ObjectMapper mapper = new ObjectMapper();
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKER);
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

            ProducerRecord<String, String> producerRecord;
            try (KafkaProducer kafkaProducer = new KafkaProducer<>(properties)
            ) {
                ArrayList<StudentBean> beans = new ArrayList<>();
                while (resultSet.next()) {
                    StudentBean bean = new StudentBean();
                    bean.setId(resultSet.getInt("id"));
                    bean.setFirst(resultSet.getString("firstname"));
                    bean.setLast(resultSet.getString("lastname"));
                    bean.setMail(resultSet.getString("email"));
                    beans.add(bean);
                    if (beans.size() > 1) {
                        String value = mapper.writeValueAsString(beans);
//                        System.out.println(value);
                        producerRecord = new ProducerRecord<>("student", "key", value);
                        kafkaProducer.send(producerRecord, new myCallback());
                        beans.clear();
                    }
                }
                resultSet.close();
                kafkaProducer.close();
                statement.close();
                connection.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    class myCallback implements Callback {
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
            if (e != null) {
                System.out.println("error in sending data");
            }

        }
    }

}
