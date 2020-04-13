package com.atguigu.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @Author lw
 * @Create2020-03-29 20:43
 */
public class CounterInterceptor implements ProducerInterceptor<String,String> {

    private long successNum = 0L;
    private long errorNum = 0L;

    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }

    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if(e == null){
            successNum++;
        }else{
            errorNum++;
        }
    }

    public void close() {

        System.out.println("successNum="+successNum);
        System.out.println("errorNum="+errorNum);
    }

    public void configure(Map<String, ?> map) {

    }
}
