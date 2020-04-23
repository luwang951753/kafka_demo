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
 *
 * 5.拦截器的获取
 *      this.interceptors = interceptorList.isEmpty() ? null : new ProducerInterceptors<>(interceptorList);
 *     需要配置ProducerConfig.INTERCEPTOR_CLASSES_CONFIG=List<String>
 *     自定义的拦截器必须是ProducerIntegerCeptor类型
 */
public class MyProducer {
    public static void main(String[] args) {

        ArrayList<String> interceptors = new ArrayList<String>();
        interceptors.add("com.atguigu.kafka.interceptor.TimeInterceptor");
        interceptors.add("com.atguigu.kafka.interceptor.CounterInterceptor");

        Properties props = new Properties();

        props.put("bootstrap.servers", "hadoop102:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        //设置自定义分区组件
        props.put("partitioner.class", "com.atguigu.kafka.producer.MyPartitioner" );
        props.put("interceptor.classes",interceptors);


        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        for (int i = 0; i < 10; i++){
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("first",null,"aa"+i,"atguigu"+i);
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
