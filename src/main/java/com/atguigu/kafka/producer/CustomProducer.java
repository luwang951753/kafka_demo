package com.atguigu.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.Properties;

/**
 * @Author lw
 * @Create2020-03-29 17:50
 */
public class CustomProducer {

    public static void main(String[] args) {

        Properties props = new Properties();


        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop102:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG,1);

        //指定拦截器
        ArrayList<String> interceptors = new ArrayList<String>();
        interceptors.add("com.atguigu.kafka.interceptor.CounterInterceptor");
        interceptors.add("com.atguigu.kafka.interceptor.TimeInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,interceptors);



        //1.创建1个生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        //2.调用send方法
        for (int i = 0; i < 10000 ; i++) {
            System.out.println(i);
            producer.send(new ProducerRecord<String, String>("first",1,i+"","中华人民共和国-"+i));
        }

        //关闭生产者
        producer.close();



    }

}
