package org.test.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @description: 自定义分区器
 * @Title: CustomPartitioner
 * @Author xlw
 * @Package org.test.kafka
 * @Date 2023/8/25 18:31
 */
public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String k = (String) key;
        if ("xlw".equals(k)) {
            return 1;
        }
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
