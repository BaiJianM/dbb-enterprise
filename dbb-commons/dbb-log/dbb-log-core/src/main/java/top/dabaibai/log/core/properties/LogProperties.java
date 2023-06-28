package top.dabaibai.log.core.properties;

import top.dabaibai.log.core.function.CustomFunctionObjectDiff;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @description: 自定义日志记录属性配置
 * @author: 白剑民
 * @dateTime: 2022-09-02 17:24:09
 */
@Data
@ConfigurationProperties(prefix = "dbb.log-record")
public class LogProperties {

    /**
     * 异步记录日志时的线程池配置
     */
    private ThreadPoolProperties threadPool = new ThreadPoolProperties();

    /**
     * 对象属性变更时的描述
     */
    private String diffMsgFormat = CustomFunctionObjectDiff.DEFAULT_DIFF_MSG_FORMAT;

    /**
     * 新增对象属性时的描述
     */
    private String addMsgFormat = CustomFunctionObjectDiff.DEFAULT_ADD_MSG_FORMAT;

    /**
     * 对象属性键值对映射描述，第一个属性为键，第二个属性为值
     */
    private String msgFormat = CustomFunctionObjectDiff.DEFAULT_MSG_FORMAT;

    /**
     * 多个对象属性键值对转字符串的分隔符
     */
    private String msgSeparator = CustomFunctionObjectDiff.DEFAULT_MSG_SEPARATOR;

    /**
     * 在方法中主动抛出异常时的操作日志生成策略（默认不继续生成任何操作日志，注：未知异常（非DbbException）的情况下，不进行日志生成）
     */
    private boolean onError = false;

    /**
     * @description: 线程池属性
     * @author: 白剑民
     * @dateTime: 2022-09-02 18:19:33
     */
    @Data
    public static class ThreadPoolProperties {

        /**
         * 线程池核心线程数
         */
        private int poolSize = 4;

        /**
         * 是否启用线程池
         */
        private boolean enabled = true;
    }

}
