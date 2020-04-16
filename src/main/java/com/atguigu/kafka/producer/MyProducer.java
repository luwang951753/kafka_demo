package com.atguigu.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * 1. 每个记录在发送时，先经过拦截器链处理，之后再发送给kafka
 *
 * 2. 在拦截器处理后，及key-value进行序列化，序列化后，计算当前recorde的分区号
 *          默认，根据指定的分区，进行计算!
 *              如果没有使用partitioner组件进行计算
 *                  组件通过配置文件中的参数ProducerConfig.PARTITIONER_CLASS_CONFIG获取，但是必须是一个Partitioner.class类型
 * this.partitioner = config.getConfiguredInstance(ProducerConfig.PARTITIONER_CLASS_CONFIG, Partitioner.class);
 *
 * 3. ProducerConfigr类提供很多静态属性，可以通过调用这个类的静态属性为producer设置参数也是可以的
 *
 * 4. 默认使用DefaultPartitioner作为分区组件
 */
public class MyProducer {
    public static void main(String[] args) {

        Properties props = new Properties();

        props.put("bootstrap.servers", "hadoop102:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //设置自定义分区组件
        props.put("ProducerConfig.PARTITIONER_CLASS_CONFIG", "com.atguigu.kafka.producer.MyPartitioner" );


        KafkaProducer<Integer, String> producer = new KafkaProducer<Integer, String>(props);

        for (int i = 0; i < 100; i++){
            ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>("first",null,i,"atguigu"+i);
            //在发送时，会先调用拦截器
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if(null == exception){
                        System.out.println("topic:"+metadata.topic()+"---partition:"+metadata.partition()+"--offset:"+metadata.offset());
                    }else{
                        exception.printStackTrace();
                    }
                }
            });
        }

        producer.close();

    }

}
