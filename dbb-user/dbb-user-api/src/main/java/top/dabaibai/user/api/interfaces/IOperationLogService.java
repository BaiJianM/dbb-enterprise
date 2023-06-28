package top.dabaibai.user.api.interfaces;

import top.dabaibai.log.core.pojo.LogDTO;
import top.dabaibai.log.core.service.ILogService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(contextId = "IOperationLogService", name = "dbb-user", path = "/user", primary = false)
public interface IOperationLogService extends ILogService {

    /**
     * @param dto 日志信息
     * @description: 操作日志入库
     * @author: 白剑民
     * @date: 2023-04-06 16:50:10
     * @version: 1.0
     */
    @Override
    @PostMapping(value = "/log")
    void createLog(@RequestBody LogDTO dto);

}
