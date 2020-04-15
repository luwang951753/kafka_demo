package com.atguigu.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * @Author lw
 * @Create2020-03-29 18:26
 */
public class ProducerCallback {
    public static void main(String[] args) {

        Properties props = new Properties();


        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"hadoop102:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG,1);


        //1.创建1个生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        //2.调用send方法
        for (int i = 0; i < 1000 ; i++) {
            producer.send(new ProducerRecord<String, String>("first", i + "", "message-" + i), new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e == null){
                    System.out.println("success");
                }else{
                    System.out.println("error");
                    e.printStackTrace();
                }
                }
            });
        }

        //关闭生产者
        producer.close();



    }
}
