package top.dabaibai.database.datapermission;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 存储需要过滤的数据配置信息在父子线程中变量共享
 * @author: 白剑民
 * @dateTime: 2023/4/6 10:49
 */
@Slf4j
public final class DataScopeParamContentHolder {

    private DataScopeParamContentHolder() {
    }

    private static final ThreadLocal<DataScopeParam> THREAD_PMS_HOLDER = new TransmittableThreadLocal<>();

    /**
     * @param dataScopeParam 数据权限属性
     * @description: 设置当前holder中的权限
     * @author: 白剑民
     * @date: 2023-04-07 10:03:37
     * @version: 1.0
     */
    public static void set(DataScopeParam dataScopeParam) {
        THREAD_PMS_HOLDER.set(dataScopeParam);
    }

    /**
     * @description: 获取holder中的权限
     * @author: 白剑民
     * @date: 2023-04-07 10:03:25
     * @return: top.dabaibai.mybatisplus.datapermission.DataScopeParam
     * @version: 1.0
     */
    public static DataScopeParam get() {
        return THREAD_PMS_HOLDER.get();
    }

    public static void clear() {
        THREAD_PMS_HOLDER.remove();
    }
}
