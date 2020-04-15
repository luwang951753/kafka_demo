package com.atguigu.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @Author lw
 * @Create2020-03-29 18:45
 */
public class SyncProducer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Properties props = new Properties();


        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop102:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG,10);


        //1.创建1个生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        //2.调用send方法
        for (int i = 0; i < 1000 ; i++) {
            System.out.println(i);
            RecordMetadata meta = producer.send(new ProducerRecord<String, String>("first", i + "", "message-" + i)).get();
            System.out.println("meta="+meta.offset());
        }

        //关闭生产者
        producer.close();



    }
}
