package com.chris.util;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: zhangliangfu
 * @Create on: 2019-03-22 13:58
 */
public class KafkaSend {

    KafkaTemplate<Integer, String> kafkaTemplate;

    public KafkaSend(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.KAFKA_SERVER_URL + ":" + KafkaProperties.KAFKA_SERVER_PORT);
        configs.put(ProducerConfig.BATCH_SIZE_CONFIG, 4096);
        configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 40960);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer .class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer .class.getName());
        DefaultKafkaProducerFactory defaultKafkaProducerFactory = new DefaultKafkaProducerFactory(configs);
        kafkaTemplate = new KafkaTemplate<Integer, String>(defaultKafkaProducerFactory
                ,true);
    }

    public void send(String topic, int key, String content){
        kafkaTemplate.send(topic,key,content);
    }

    public static void main(String[] args) {
        KafkaSend kafkaSend = new KafkaSend();
        kafkaSend.send("test", 1, "sss" );
    }

}
