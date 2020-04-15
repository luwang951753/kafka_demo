package com.atguigu.kafka1;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CounterInterceptor implements ProducerInterceptor<String, String> {
    private int errorCounter = 0;
    private int successCount = 0;
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        return producerRecord;
    }

    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if(e == null){
            successCount++;
        }else{
            errorCounter++;
        }
    }

    public void close() {
        System.out.println("Successful sendt:"+successCount);
        System.out.println("Failed sent:"+errorCounter);
    }

    public void configure(Map<String, ?> map) {

    }
}
