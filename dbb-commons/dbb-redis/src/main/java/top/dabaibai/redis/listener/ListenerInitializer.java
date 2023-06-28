package top.dabaibai.redis.listener;

import top.dabaibai.redis.annotation.RedisChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 广播监听器初始化
 * @author: 白剑民
 * @dateTime: 2023/4/17 14:25
 */
@Slf4j
public class ListenerInitializer implements ApplicationListener<ApplicationEvent> {

    /**
     * 初始化标记，仅初始化时执行一次
     */
    private boolean initTag;

    /**
     * 频道名称与监听实现类的映射
     */
    private Map<String, MessageListener> listeners;

    private final ApplicationContext applicationContext;

    public ListenerInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        if (!initTag) {
            // 获取所有实现
            Map<String, MessageListener> listenerImpls = applicationContext.getBeansOfType(MessageListener.class);
            // 定义频道名称与监听实现类的映射
            Map<String, MessageListener> listeners = new HashMap<>(16);
            listenerImpls.forEach((s, messageListener) -> {
                RedisChannel channel = messageListener.getClass().getAnnotation(RedisChannel.class);
                if (channel == null) {
                    throw new RuntimeException(messageListener.getClass().getSimpleName() +
                            "实现了MessageListener接口，但是类上没有标注@RedisChannel注解以注明监听频道");
                } else {
                    listeners.put(channel.name(), messageListener);
                }
            });
            this.listeners = listeners;
            initTag = true;
        }
    }

    /**
     * @description: 获取所有频道名称与监听实现类的映射
     * @author: 白剑民
     * @date: 2023-04-17 14:36:52
     * @return: java.util.Map<java.lang.String, org.springframework.data.redis.connection.MessageListener>
     * @version: 1.0
     */
    public Map<String, MessageListener> getListeners() {
        return listeners;
    }
}
