package com.gientech.iot.log.core.context;

import com.gientech.iot.log.core.pojo.DiffVO;
import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 日志记录上下文
 * @author: 白剑民
 * @dateTime: 2022-09-02 17:49:32
 */
public class LogContext {

    /**
     * 本地上下文线程
     */
    private static final ThreadLocal<StandardEvaluationContext> CONTEXT_THREAD_LOCAL =
            new NamedThreadLocal<>("ThreadLocal StandardEvaluationContext");

    /**
     * 发生变化的列表数据集合 本地线程
     */
    private static final ThreadLocal<List<DiffVO>> DIFF_DTO_LIST_THREAD_LOCAL =
            new NamedThreadLocal<>("ThreadLocal DiffVOList");

    /**
     * 上下文获取方法返回的key
     */
    public static final String CONTEXT_KEY_NAME_RETURN = "_return";

    /**
     * 上下文获取错误信息的key
     */
    public static final String CONTEXT_KEY_NAME_ERROR_MSG = "_errorMsg";

    /**
     * @description: 获取上下文
     * @author: 白剑民
     * @date: 2022-09-03 22:42:12
     * @return: @return {@code StandardEvaluationContext }
     * @version: 1.0
     */
    public static StandardEvaluationContext getContext() {
        return CONTEXT_THREAD_LOCAL.get() == null ? new StandardEvaluationContext(): CONTEXT_THREAD_LOCAL.get();
    }

    /**
     * @param key   关键
     * @param value 价值
     * @description: 设置上下文变量信息
     * @author: 白剑民
     * @date: 2022-09-03 22:42:17
     * @return:
     * @version: 1.0
     */
    public static void putVariables(String key, Object value) {
        StandardEvaluationContext context = getContext();
        context.setVariable(key, value);
        CONTEXT_THREAD_LOCAL.set(context);
    }

    /**
     * @description: 清除上下文信息
     * @author: 白剑民
     * @date: 2022-09-03 22:42:34
     * @return:
     * @version: 1.0
     */
    public static void clearContext() {
        CONTEXT_THREAD_LOCAL.remove();
    }

    /**
     * @description: 获取发生变化的数据信息集合
     * @author: 白剑民
     * @date: 2022-09-03 22:43:53
     * @return: @return {@code List<DiffDTO> }
     * @version: 1.0
     */
    public static List<DiffVO> getDiffVOList() {
        return DIFF_DTO_LIST_THREAD_LOCAL.get() == null ? new ArrayList<>() : DIFF_DTO_LIST_THREAD_LOCAL.get();
    }

    /**
     * @param diffVO 发生变化的数据
     * @description: 添加发生变化的数据
     * @author: 白剑民
     * @date: 2022-09-03 22:44:25
     * @return:
     * @version: 1.0
     */
    public static void addDiffVO(DiffVO diffVO) {
        if (diffVO != null) {
            List<DiffVO> diffVOList = getDiffVOList();
            diffVOList.add(diffVO);
            DIFF_DTO_LIST_THREAD_LOCAL.set(diffVOList);
        }
    }

    /**
     * @description: 清除发生变化的数据集合
     * @author: 白剑民
     * @date: 2022-09-03 22:44:46
     * @return:
     * @version: 1.0
     */
    public static void clearDiffVOList() {
        DIFF_DTO_LIST_THREAD_LOCAL.remove();
    }


}
