package com.atguigu.kafka1;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CustomProducer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers","hadoop102:9092");
        props.put("acks","all");
        props.put(ProducerConfig.RETRIES_CONFIG,1);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,1024);
        props.put(ProducerConfig.LINGER_MS_CONFIG,1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");


        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 0; i <10000000 ; i++) {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("first",1,"name","lw"+i);
            producer.send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if(e == null){
                        System.out.println("success->"+recordMetadata.toString());
                    }else{
                        e.printStackTrace();
                    }
                }
            });
        }

        producer.close();


    }
}
