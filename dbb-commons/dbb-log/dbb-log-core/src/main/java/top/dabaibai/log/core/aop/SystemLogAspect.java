package top.dabaibai.log.core.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import top.dabaibai.core.pojo.vo.BaseUserInfoVO;
import top.dabaibai.core.utils.IpUtils;
import top.dabaibai.core.utils.SpringContextUtils;
import top.dabaibai.core.utils.UserInfoUtils;
import top.dabaibai.log.annotations.OperationLog;
import top.dabaibai.log.core.pojo.LogDTO;
import top.dabaibai.log.core.properties.LogProperties;
import top.dabaibai.log.core.context.LogContext;
import top.dabaibai.log.core.function.CustomFunctionRegistrar;
import top.dabaibai.log.core.service.ILogService;
import top.dabaibai.web.commons.http.DbbException;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @description: 系统日志切面
 * @author: 白剑民
 * @dateTime: 2022-09-01 17:49:18
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect {

    /**
     * 日志记录线程池
     */
    private final ExecutorService logThreadPool;
    private static final ThreadFactory THREAD_FACTORY = new CustomizableThreadFactory("log-record-");

    /**
     * 操作日志服务
     */
    private final ILogService logService;

    /**
     * 自定义日志配置
     */
    private final LogProperties properties;

    /**
     * Spel解析器
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * 参数名发现者
     */
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    public SystemLogAspect(LogProperties logProperties, ILogService logService) {
        if (logProperties.getThreadPool().isEnabled()) {
            log.info("LogRecordThreadPool init poolSize [{}]", logProperties.getThreadPool().getPoolSize());
            int poolSize = logProperties.getThreadPool().getPoolSize();
            this.logThreadPool = new ThreadPoolExecutor(
                    poolSize, poolSize, 0L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.CallerRunsPolicy());
        } else {
            this.logThreadPool = null;
        }
        this.logService = logService;
        this.properties = logProperties;
    }

    /**
     * @param pjp 切点信息
     * @description: 环绕通知
     * @author: 白剑民
     * @date: 2022-09-02 16:29:36
     * @return: @return {@code Object }
     * @version: 1.0
     */
    @Around("@annotation(top.dabaibai.log.annotations.OperationLog) || @annotation(top.dabaibai.log.annotations.OperationLogs)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 客户端信息
        UserAgent userAgent = UserAgent.parseUserAgentString(SpringContextUtils.getRequest().getHeader("User-Agent"));
        String ipAddress = IpUtils.getIpAddress(SpringContextUtils.getRequest());
        Long systemId = UserInfoUtils.getSystemId();
        // 定义返回结果
        Object result;
        // 定义@OperationLog注解数组
        OperationLog[] annotations;
        // 定义LogDTO集合，用于存放注解解析后的LogDTO对象
        List<LogDTO> logDTOList = new ArrayList<>();
        // 定义有序的Map，用于存放注解解析后的LogDTO对象
        Map<OperationLog, LogDTO> logDtoMap = new LinkedHashMap<>();
        // 耗时监听对象
        StopWatch stopWatch = null;
        // 执行耗时
        Long executionTime = null;
        // 注解解析：若解析失败直接不执行日志切面逻辑
        try {
            // 获取切面方法
            Method method = getMethod(pjp);
            // 获取方法上的@OperationLog或@OperationLogs注解信息
            annotations = method.getDeclaredAnnotationsByType(OperationLog.class);
        } catch (Throwable throwable) {
            // 若发生异常，则直接返回切面方法执行结果
            return pjp.proceed();
        }
        // 定义配置了executeBeforeFunc == true 的执行方法后解析切面逻辑的注解集合
        List<OperationLog> afterExec = new ArrayList<>();
        // 下方是日志切面逻辑
        try {
            // 方法执行前日志切面
            // 否则遍历注解集合
            for (OperationLog annotation : annotations) {
                // 执行方法前解析切面逻辑
                if (annotation.executeBeforeFunc()) {
                    // 解析注解中定义的Spel表达式，返回LogDTO对象
                    LogDTO logDTO = resolveExpress(annotation, pjp);
                    if (logDTO != null) {
                        // 若解析后的logDto数据不为空，则将logDto放入Map中
                        logDtoMap.put(annotation, logDTO);
                    }
                } else {
                    afterExec.add(annotation);
                }
            }
            // 初始化耗时监听对象
            stopWatch = new StopWatch();
            // 开启计时
            stopWatch.start();
            // 原方法执行
            result = pjp.proceed();
            // 方法成功执行后日志切面
            // 如果执行了前置解析切面逻辑的注解后，不存在后置解析的注解了就返回原方法执行结果
            if (afterExec.size() == 0) {
                // 否则继续执行后置解析切面注解
                stopWatch.stop();
                executionTime = stopWatch.getTotalTimeMillis();
            } else {
                // 在LogContext中写入执行后信息，将切面方法执行的返回结果写入日志记录上下文中
                LogContext.putVariables(LogContext.CONTEXT_KEY_NAME_RETURN, result);
                // 遍历配置了executeBeforeFunc == true 的执行方法后解析切面逻辑的注解集合
                afterExec.forEach(annotation -> {
                    // 解析注解中定义的Spel表达式，返回LogDTO对象
                    LogDTO logDTO = resolveExpress(annotation, pjp);
                    if (logDTO != null) {
                        // 若解析后的logDTO数据不为空，则将logDTO放入Map中
                        logDtoMap.put(annotation, logDTO);
                    }
                });
            }
            // 写入成功执行后日志
            logDTOList = new ArrayList<>(logDtoMap.values());
            // 遍历logDtoMap
            logDtoMap.forEach((annotation, logDTO) -> {
                if (logDTO.getSuccess() == null) {
                    logDTO.setSuccess(true);
                }
                // 若需要记录返回值，则logDTO.setReturnStr切面方法返回值的JSONString
                if (annotation.recordReturnValue()) {
                    logDTO.setReturnStr(JSON.toJSONString(result));
                }
            });
        }
        // 原方法执行异常
        catch (Throwable throwable) {
            // 方法异常执行后日志切面
            // 记录日志切面执行耗时
            if (stopWatch != null) {
                // 结束计时
                stopWatch.stop();
                // 获取执行耗时
                executionTime = stopWatch.getTotalTimeMillis();
            }
            // 在LogContext中写入执行后信息，将切面方法执行的异常信息写入日志记录上下文中
            LogContext.putVariables(LogContext.CONTEXT_KEY_NAME_ERROR_MSG, throwable.getMessage());
            // 如果配置了方法执行异常后继续生成操作日志的策略则继续执行(如果配置了继续生成日志的策略，则异常仍必须为主动异常类型才执行)
            if (properties.isOnError() && throwable instanceof DbbException) {
                // 遍历执行方法后解析切面逻辑
                afterExec.forEach(annotation -> {
                    // 解析注解中定义的Spel表达式，返回LogDTO对象
                    LogDTO logDTO = resolveExpress(annotation, pjp);
                    if (logDTO != null) {
                        // 若解析后的logDTO数据不为空，则将logDTO放入Map中
                        logDtoMap.put(annotation, logDTO);
                    }
                });
                // 写入异常执行后日志
                logDTOList = new ArrayList<>(logDtoMap.values());
                // 遍历日志信息LogDTO集合
                logDTOList.forEach(logDTO -> {
                    // 设置执行结果为false，
                    logDTO.setSuccess(false);
                    // 写入执行异常信息
                    logDTO.setException(throwable.getMessage());
                });
            }
            // 抛出原方法异常
            throw throwable;
        } finally {
            try {
                // 提交日志至主线程或线程池
                Long finalExecutionTime = executionTime;
                Consumer<LogDTO> createLogFunction = logDTO -> {
                    try {
                        // 记录日志切面执行时间
                        logDTO.setExecutionTime(finalExecutionTime);
                        // 发送日志本地监听
                        if (logService != null) {
                            logDTO.setIpAddress(ipAddress);
                            logDTO.setOs(userAgent.getOperatingSystem().getName());
                            logDTO.setBrowser(userAgent.getBrowser().getName());
                            logDTO.setSystemId(systemId);
                            logService.createLog(logDTO);
                        }
                    } catch (Throwable throwable) {
                        log.error("操作日志提交至线程执行存储时失败: {}", throwable.getMessage());
                    }
                };
                // 判断是否使用日志记录线程池
                if (logThreadPool != null) {
                    // 使用线程池异步处理日志
                    logDTOList.forEach(logDTO -> logThreadPool.submit(() -> createLogFunction.accept(logDTO)));
                } else {
                    // 同步处理日志
                    logDTOList.forEach(createLogFunction);
                }
                // 清除Context：每次方法执行一次
                LogContext.clearContext();
            } catch (Throwable throwableFinal) {
                log.error("操作日志最终执行失败: {}", throwableFinal.getMessage());
            }
        }
        return result;
    }

    /**
     * @param annotation 注释
     * @param joinPoint  连接点
     * @description: 解析注解中定义的spel表达式
     * @author: 白剑民
     * @date: 2022-09-02 16:30:21
     * @return: @return {@code LogDTO }
     * @version: 1.0
     */
    private LogDTO resolveExpress(OperationLog annotation, JoinPoint joinPoint) {
        // 定义LogDTO对象，Spel解析后的对象
        LogDTO logDTO = null;
        // 业务ID，SpEL表达式
        String bizId = annotation.bizId();
        // 业务名称，SpEL表达式
        String bizName = annotation.bizName();
        // 业务类型，SpEL表达式
        String bizType = annotation.bizType();
        // 业务事件，SpEL表达式
        String bizEvent = annotation.bizEvent();
        // 日志标签，SpEL表达式
        String tag = annotation.tag();
        // 日志内容，SpEL表达式
        String msg = annotation.msg();
        // 额外信息，SpEL表达式
        String extra = annotation.extra();
        // 操作人ID，SpEL表达式
        String operatorId = annotation.operatorId();
        // 操作人名称，SpEL表达式
        String operatorName = annotation.operatorName();
        // 操作人编号，SpEL表达式
        String operatorCode = annotation.operatorCode();
        // 日志记录条件，SpEL表达式
        String condition = annotation.condition();
        // 执行是否成功，SpEL表达式
        String success = annotation.success();

        // 执行是否成功，SpEL解析结果，默认为null
        Boolean functionExecuteSuccess = null;

        try {
            // 获取切面的方法入参
            Object[] arguments = joinPoint.getArgs();
            // 获取切面方法
            Method method = getMethod(joinPoint);
            // 获取切面方法的参数名
            String[] params = discoverer.getParameterNames(method);
            // 获取日志记录上下文
            StandardEvaluationContext context = LogContext.getContext();
            // 注册自定义函数
            CustomFunctionRegistrar.register(context);
            if (params != null) {
                // 编辑方法参数，将参数放入日志记录上下文中
                for (int len = 0; len < params.length; len++) {
                    context.setVariable(params[len], arguments[len]);
                }
            }

            // condition 处理：SpEL解析 必须符合表达式
            if (StringUtils.isNotBlank(condition)) {
                Expression conditionExpression = parser.parseExpression(condition);
                boolean passed = Boolean.TRUE.equals(conditionExpression.getValue(context, Boolean.class));
                if (!passed) {
                    return null;
                }
            }

            // success 处理：SpEL解析 必须符合表达式
            if (StringUtils.isNotBlank(success)) {
                Expression successExpression = parser.parseExpression(success);
                functionExecuteSuccess = Boolean.TRUE.equals(successExpression.getValue(context, Boolean.class));
            }

            // bizId 处理：SpEL解析 必须符合表达式
            if (StringUtils.isNotBlank(bizId)) {
                Expression bizIdExpression = parser.parseExpression(bizId);
                bizId = bizIdExpression.getValue(context, String.class);
            }

            // bizName 处理：SpEL解析 必须符合表达式
            if (StringUtils.isNotBlank(bizName)) {
                Expression bizNameExpression = parser.parseExpression(bizName);
                bizName = bizNameExpression.getValue(context, String.class);
            }

            // bizType 处理：SpEL解析 必须符合表达式
            if (StringUtils.isNotBlank(bizType)) {
                Expression bizTypeExpression = parser.parseExpression(bizType);
                bizType = bizTypeExpression.getValue(context, String.class);
            }

            // bizEvent 处理：SpEL解析 必须符合表达式
            if (StringUtils.isNotBlank(bizEvent)) {
                Expression bizEventExpression = parser.parseExpression(bizEvent);
                bizEvent = bizEventExpression.getValue(context, String.class);
            }

            // tag 处理：SpEL解析 必须符合表达式
            if (StringUtils.isNotBlank(tag)) {
                Expression tagExpression = parser.parseExpression(tag);
                tag = tagExpression.getValue(context, String.class);
            }

            // msg 处理：SpEL解析 必须符合表达式 若为实体则JSON序列化实体
            if (StringUtils.isNotBlank(msg)) {
                Expression msgExpression = parser.parseExpression(msg);
                Object msgObj = msgExpression.getValue(context, Object.class);
                msg = msgObj instanceof String
                        ? (String) msgObj : JSON.toJSONString(msgObj, SerializerFeature.WriteMapNullValue);
            }

            // extra 处理：SpEL解析 必须符合表达式 若为实体则JSON序列化实体
            if (StringUtils.isNotBlank(extra)) {
                Expression extraExpression = parser.parseExpression(extra);
                Object extraObj = extraExpression.getValue(context, Object.class);
                extra = extraObj instanceof String
                        ? (String) extraObj : JSON.toJSONString(extraObj, SerializerFeature.WriteMapNullValue);
            }

            BaseUserInfoVO userInfo = UserInfoUtils.getUserInfo();

            // operatorId 处理：优先级 注解传入 > 自定义接口实现
            // 必须符合表达式
            if (StringUtils.isNotBlank(operatorId)) {
                Expression operatorIdExpression = parser.parseExpression(operatorId);
                operatorId = operatorIdExpression.getValue(context, String.class);
            } else {
                operatorId = userInfo.getUserId() == null ? "0" : String.valueOf(userInfo.getUserId());
            }

            // operatorName 处理：优先级 注解传入 > 自定义接口实现
            // 必须符合表达式
            if (StringUtils.isNotBlank(operatorName)) {
                Expression operatorNameExpression = parser.parseExpression(operatorName);
                operatorName = operatorNameExpression.getValue(context, String.class);
            } else {
                if (userInfo.getRealName() != null) {
                    operatorName = userInfo.getRealName();
                }
            }

            // operatorCode 处理：优先级 注解传入 > 自定义接口实现
            // 必须符合表达式
            if (StringUtils.isNotBlank(operatorCode)) {
                Expression operatorNameExpression = parser.parseExpression(operatorCode);
                operatorCode = operatorNameExpression.getValue(context, String.class);
            } else {
                if (userInfo.getCode() != null) {
                    operatorCode = userInfo.getCode();
                }
            }

            // 封装日志对象 logDTO
            logDTO = new LogDTO();
            logDTO.setBizId(bizId);
            logDTO.setBizName(bizName);
            logDTO.setBizType(bizType);
            logDTO.setBizEvent(bizEvent);
            logDTO.setTag(tag);
            logDTO.setOperateDate(new Date());
            logDTO.setMsg(msg);
            logDTO.setExtra(extra);
            logDTO.setOperatorId(operatorId);
            logDTO.setOperatorName(operatorName);
            logDTO.setOperatorCode(operatorCode);
            logDTO.setSuccess(functionExecuteSuccess);
            logDTO.setDiffVOList(LogContext.getDiffVOList());
        } catch (Exception e) {
            log.error("操作日志SPEL表达式解析失败: {}", e.getMessage());
        } finally {
            // 清除Diff实体列表：每次注解执行一次
            LogContext.clearDiffVOList();
        }
        return logDTO;
    }

    /**
     * @param joinPoint 切点
     * @description: 获取切点方法
     * @author: 白剑民
     * @date: 2022-09-03 22:35:36
     * @return: @return {@code Method }
     * @version: 1.0
     */
    private Method getMethod(JoinPoint joinPoint) {
        Method method = null;
        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature ms = (MethodSignature) signature;
            Object target = joinPoint.getTarget();
            method = target.getClass().getMethod(ms.getName(), ms.getParameterTypes());
        } catch (NoSuchMethodException e) {
            log.error("获取切面方法时出错: {}", e.getMessage());
        }
        return method;
    }
}
