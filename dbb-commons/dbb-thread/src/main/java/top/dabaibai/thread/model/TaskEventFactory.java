package top.dabaibai.thread.model;

import com.lmax.disruptor.EventFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/7/30 21:15
 */
@Slf4j
public class TaskEventFactory implements EventFactory<Task> {
    @Override
    public Task newInstance() {
        return new Task();
    }
}
