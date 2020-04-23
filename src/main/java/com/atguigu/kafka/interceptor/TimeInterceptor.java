package com.atguigu.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @Author lw
 * @Create2020-03-29 20:37
 */
public class TimeInterceptor implements ProducerInterceptor<String,String> {

    //从producer配置文件中获取属性
    public void configure(Map<String, ?> map) {

    }

    //拦截处理record
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return new ProducerRecord<String, String>(
                record.topic(),
                record.partition(),
                record.timestamp(),
                record.key(),
                System.currentTimeMillis()+record.value(),
                record.headers());

    }

    //收到broker的ack后调用
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    //producer执行close()调用此方法
    public void close() {

    }


}
