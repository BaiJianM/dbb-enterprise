package top.dabaibai.redis;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.dabaibai.redis.listener.ListenerInitializer;
import top.dabaibai.redis.properties.CustomSpringCacheProperties;
import top.dabaibai.redis.properties.RedisCustomProperties;
import top.dabaibai.redis.utils.RedisUtils;

import java.time.Duration;
import java.util.Map;

/**
 * @description: Redis配置类（使用Lettuce客户端连接）
 * @author: 白剑民
 * @dateTime: 2022/7/8 08:46
 */
@Slf4j
@Configuration
@EnableCaching
@Import({ListenerInitializer.class, RedisUtils.class})
@EnableConfigurationProperties({RedisCustomProperties.class, CustomSpringCacheProperties.class})
public class RedisAutoConfiguration {
    /**
     * @param factory 使用lettuce客户端连接
     * @description: 配置模板客户端
     * @author: 白剑民
     * @date: 2022-08-18 15:36:31
     * @return: org.springframework.data.redis.core.RedisTemplate<java.lang.String, java.lang.Object>
     * @version: 1.0
     */
    @Bean
    public RedisTemplate<String, Object> initRedisTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = createRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key 采用 String 的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash 的 key 也采用 String 的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value 序列化方式采用 jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash 的 value 序列化方式采用 jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * @param connectionFactory redis连接
     * @param initializer       消息监听初始化执行器
     * @description: 初始化消息监听容器
     * @author: 白剑民
     * @date: 2022-08-18 15:56:38
     * @return: org.springframework.data.redis.listener.RedisMessageListenerContainer
     * @version: 1.0
     */
    @Bean
    public RedisMessageListenerContainer container(LettuceConnectionFactory connectionFactory,
                                                   ListenerInitializer initializer) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 获取频道名称与监听实现类的映射
        Map<String, MessageListener> listeners = initializer.getListeners();
        if (MapUtil.isNotEmpty(listeners)) {
            // 遍历映射集，注册监听器到RedisMessageListenerContainer
            listeners.forEach((channel, listener) -> {
                MessageListenerAdapter adapter = new MessageListenerAdapter(listener, "onMessage");
                container.addMessageListener(adapter, new PatternTopic(channel));
            });
            log.debug("订阅频道: " + listeners.keySet());
        }
        return container;
    }

    /**
     * @param factory    lettuce客户端连接
     * @param properties 自定义配置类
     * @description: 初始化Redis自定义配置作为Spring Cache的缓存管理器实现
     * @author: 白剑民
     * @date: 2023-05-12 23:13:06
     * @return: org.springframework.data.redis.cache.RedisCacheManager
     * @version: 1.0
     */
    @Bean("redisCacheManager")
    @ConditionalOnProperty(prefix = "dbb.redis", name = "use-spring-cache", havingValue = "true")
    public RedisCacheManager initRedisCacheManager(LettuceConnectionFactory factory, RedisCustomProperties properties) {
        // 获取Redis加锁的写入器
        RedisCacheWriter rcw = RedisCacheWriter.lockingRedisCacheWriter(factory);
        // 启动Redis缓存的默认设置
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        // 设置序列化器
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = createRedisSerializer();
        configuration = configuration.serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        configuration = configuration.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
        // 禁用前缀
        if (!properties.getSpringCacheProperties().isUseKeyPrefix()) {
            configuration = configuration.disableKeyPrefix();
        }
        // 如果使用前缀，则将key后缀中的两个::号改为一个:
        else {
            configuration = configuration.computePrefixWith(cacheName -> cacheName + ":");
        }
        // 设置key超时时间
        int keyTtl = properties.getSpringCacheProperties().getKeyTtl();
        if (keyTtl != -1) {
            configuration = configuration.entryTtl(Duration.ofMillis(keyTtl));
        }
        // 创建Redis缓存管理器
        return new RedisCacheManager(rcw, configuration);
    }

    /**
     * @description: 以jackson作为redis的序列化器
     * @author: 白剑民
     * @date: 2023-05-12 23:10:16
     * @return: org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer<java.lang.Object>
     * @version: 1.0
     */
    private static Jackson2JsonRedisSerializer<Object> createRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }
}
