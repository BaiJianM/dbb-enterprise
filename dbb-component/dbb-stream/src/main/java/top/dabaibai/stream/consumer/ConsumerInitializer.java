package top.dabaibai.stream.consumer;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 消费者初始化
 * @author: 白剑民
 * @dateTime: 2023/3/11 11:27
 */
@Slf4j
public class ConsumerInitializer implements ApplicationListener<ApplicationEvent> {

    /**
     * 初始化标记，仅初始化时执行一次
     */
    private boolean initTag;

    private final Set<String> tags = new HashSet<>();

    /**
     * 消费者监听类集合
     */
    private Map<String, DbbConsumerListener> consumerListeners;

    private final ApplicationContext applicationContext;

    public ConsumerInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        if (!initTag) {
            // 获取所有实现了自定义消费者接口的实现类
            Map<String, DbbConsumerListener> consumerListeners = applicationContext.getBeansOfType(DbbConsumerListener.class);
            // 校验这些实现类是否包含规定注解
            // 校验所有消费者监听的tag有没有重复
            Set<CheckTag> repeatTags = new HashSet<>();
            consumerListeners.forEach((key, value) -> {
                String className = value.getClass().getSimpleName();
                DbbMQListener annotation = value.getClass().getAnnotation(DbbMQListener.class);
                if (annotation == null) {
                    throw new RuntimeException(className +
                            "实现了DbbConsumerListener接口，但是类上没有标注@DbbMQListener注解");
                }
                Arrays.stream(annotation.tags()).forEach(t -> {
                    CheckTag tag = new CheckTag();
                    tag.setTag(t);
                    tag.setClassName(className);
                    boolean add = repeatTags.add(tag);
                    // 如果已经存在tag就抛出异常
                    if (!add) {
                        throw new RuntimeException(className +
                                "实现了DbbConsumerListener接口，但是tag存在重复定义。重复定义的类为"
                                + value.getClass().getName());
                    }
                });
            });
            Set<String> configTags = repeatTags.stream().map(CheckTag::getTag).collect(Collectors.toSet());
            this.tags.addAll(configTags);
            this.consumerListeners = consumerListeners;
            initTag = true;
        }
    }

    /**
     * @description: 获取所有实现了自定义消费者接口的实现类
     * @author: 白剑民
     * @date: 2023-03-11 12:36:41
     * @return: java.util.Map<java.lang.String, top.dabaibai.stream.consumer.DbbConsumerListener>
     * @version: 1.0
     */
    public Map<String, DbbConsumerListener> getConsumerListeners() {
        return this.consumerListeners;
    }

    public Set<String> getTags() {
        return this.tags;
    }

    @Data
    private static class CheckTag {
        /**
         * 消息tag属性
         */
        private String tag;
        /**
         * 消息tag属性所在类名
         */
        private String className;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CheckTag checkTag = (CheckTag) o;
            return tag.equals(checkTag.tag);
        }

        @Override
        public int hashCode() {
            return Objects.hash(tag);
        }
    }
}