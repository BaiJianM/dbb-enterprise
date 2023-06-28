package top.dabaibai.demo.biz.config.rabbitmq;

import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.stereotype.Component;

/**
 * @description: 自定义分区选择器（设置分区）
 * @author: 白剑民
 * @dateTime: 2023-03-10 12:47:41
 */
@Component
public class MyPartitionSelector implements PartitionSelectorStrategy {

    @Override
    public int selectPartition(Object key, int partitionCount) {
//        if (key != null) {
//            return Integer.parseInt(String.valueOf(key)) % partitionCount;
//        }
        return 0;
    }
}
