package com.atguigu.kafka.consumer;


import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.cluster.BrokerEndPoint;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * 1. 获取所消费分区的leader
 * 2. 向leader请求消费数据 /Topic/Partition/Offset
 * 3. 根据Leader返回的请求，获取消息
 */
public class MyLowerConsumer {

    public static void main(String[] args) {
        new MyLowerConsumer().getData("first", 0, "hadoop102", 9092, 1000,1000,0);



    }

    //获取数据
    public void getData(String topic, int partition, String host, int port, int soTimeout, int fetchSize,int offset ){
        //获取leader
        BrokerEndPoint leader = getLeader(topic, partition, host, port, soTimeout);

        if(leader == null){
            System.out.println("当前分区无法找到leader");
            return;
        }

        //获取一个simpleConsumer 请求leader的broker地址
        SimpleConsumer simpleConsumer = new SimpleConsumer(leader.host(), leader.port(), soTimeout, 1024*1024,"getData");

        //构建fetchRequest fetchsize为consumer中的batchsize
        FetchRequest request = new FetchRequestBuilder().addFetch(topic, partition, offset, fetchSize).build();

        //解析response获取感兴趣的数据
        FetchResponse response = simpleConsumer.fetch(request);

        ByteBufferMessageSet messageSet = response.messageSet(topic, partition);
        //MessageAndOffset 包含当前的消息和当前消息的Offset
        for (MessageAndOffset messageAndOffset : messageSet) {
            //获取offset
            System.out.println("offset==>"+messageAndOffset.offset());
            //获取消息装载的字节数组
            ByteBuffer payload = messageAndOffset.message().payload();
            byte[] message = new byte[payload.limit()];
            //从消息字节数组
            payload.get(message);
            System.out.println("message==>"+new String(message));
        }


    }

    public BrokerEndPoint getLeader(String topic, int partition, String host, int port, int soTimeout){
        //创建simpleConsumer
        SimpleConsumer simpleConsumer = new SimpleConsumer(host, port, soTimeout, 1024*1024, "getLeader");
        //创建一个topic元数据的请求
        TopicMetadataRequest request = new TopicMetadataRequest(Arrays.asList(topic));
        //请求获取元数据的响应
        TopicMetadataResponse response = simpleConsumer.send(request);
        //获取请求的所有主题的元数据列表
        List<TopicMetadata> topicMetadata = response.topicsMetadata();

        for (TopicMetadata metadata : topicMetadata) {

            System.out.println("当前主题元数据"+metadata);

            //当前主题所有分区元数据
            List<PartitionMetadata> partitionsMetadata = metadata.partitionsMetadata();
            for (PartitionMetadata partitionMetadata : partitionsMetadata) {
                if(partitionMetadata.partitionId() == partition){
                    //获取当前分区的leader
                    return partitionMetadata.leader();
                }
            }
        }
        return null;
    }
}
