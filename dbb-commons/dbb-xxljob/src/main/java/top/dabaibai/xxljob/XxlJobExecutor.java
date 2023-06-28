package top.dabaibai.xxljob;

import lombok.Data;

/**
 * @description: xxl-job执行器配置
 * @author: 白剑民
 * @dateTime: 2023/2/20 15:31
 */
@Data
public class XxlJobExecutor {
    /**
     * 应用名称
     */
    private String appname;

    /**
     * 执行器地址
     */
    private String address;

    /**
     * 执行器IP
     */
    private String ip;

    /**
     * 执行器端口
     */
    private int port;

    /**
     * 允许系统进行端口占用自检，以免端口冲突
     */
    private boolean portInUse = true;

    /**
     * 日志存放路径
     */
    private String logPath;

    /**
     * 日志保留天数
     */
    private int logRetentionDays;
}
