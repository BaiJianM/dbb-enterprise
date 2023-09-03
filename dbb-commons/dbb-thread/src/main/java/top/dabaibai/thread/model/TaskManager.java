package top.dabaibai.thread.model;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/7/30 21:18
 */
@Slf4j
@Configuration
public class TaskManager {

    @Bean("thead-model")
    public RingBuffer<Task> taskRingBuffer() {
        TaskEventFactory factory = new TaskEventFactory();
        int bufferSize = 1024 * 256;
        Disruptor<Task> disruptor = new Disruptor<>(factory, bufferSize, Executors.defaultThreadFactory(),
                ProducerType.SINGLE, new BlockingWaitStrategy());
        disruptor.handleEventsWith(new TaskEventHandler());
        disruptor.start();

        return disruptor.getRingBuffer();
    }

}
