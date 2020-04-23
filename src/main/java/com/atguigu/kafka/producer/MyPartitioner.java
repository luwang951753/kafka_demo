package com.atguigu.kafka.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class MyPartitioner implements Partitioner {

    //计算分区
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();
        System.out.println("计算自定义分区");
        return (key.hashCode() & Integer.MAX_VALUE) % numPartitions;
    }

    public void close() {
        //close是在producer执行close()时，调用相关的partition的close()

    }

    public void configure(Map<String, ?> configs) {
        //从当前Producer的配置中读取相关属性
        Object object = configs.get("batch.size");

        System.out.println("自定义分区读取配置信息:"+object);
    }
}
