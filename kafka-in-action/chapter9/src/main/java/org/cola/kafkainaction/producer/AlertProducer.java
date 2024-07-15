package org.cola.kafkainaction.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.cola.kafkainaction.callback.AlertCallback;
import org.cola.kafkainaction.model.Alert;

import java.util.Properties;

public class AlertProducer {

    public static void main(String[] args) {

        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093");
        kaProperties.put("key.serializer", "org.kafkainaction.serde.AlertKeySerde");
        kaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kaProperties.put("interceptor.classes", "org.kafkainaction.producer.AlertProducerMetricsInterceptor");

        try (Producer<Alert, String> producer = new KafkaProducer<Alert, String>(kaProperties)) {

            Alert alert = new Alert(1, "Stage 1", "CRITICAL", "Stage 1 stopped");
            ProducerRecord<Alert, String> producerRecord = new ProducerRecord<>("kinaction_alert", alert, alert.getAlertMessage()); //<1>
            producer.send(producerRecord, new AlertCallback());
        }
    }

}