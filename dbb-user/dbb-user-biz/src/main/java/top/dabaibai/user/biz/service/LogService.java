package top.dabaibai.user.biz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.dabaibai.log.core.enums.LogTypeEnum;
import top.dabaibai.user.api.pojo.dto.LogSearchDTO;
import top.dabaibai.user.api.pojo.dto.OnlineSearchDTO;
import top.dabaibai.user.api.pojo.vo.LogSearchResultVO;
import top.dabaibai.user.api.pojo.vo.OnlineSearchResultVO;
import top.dabaibai.user.biz.entity.SysLog;
import top.dabaibai.web.commons.model.PageResultVO;

import java.util.List;

/**
 * @description: 日志信息接口类
 * @author: 白剑民
 * @dateTime: 2022/11/18 15:03
 */
public interface LogService extends IService<SysLog> {

    /**
     * @param dto 系统操作日志搜索传参
     * @description: 系统操作日志搜索
     * @author: 白剑民
     * @date: 2022-11-21 09:42:36
     * @return: top.dabaibai.pojo.vo.PageResultVO<top.dabaibai.user.entity.vo.LogSearchResultVO>
     * @version: 1.0
     */
    PageResultVO<LogSearchResultVO> search(LogSearchDTO dto);

    /**
     * @param ids id
     * @description: 删除
     * @author: 白剑民
     * @date: 2023-05-29 15:09:26
     * @return: void
     * @version: 1.0
     */
    void delete(List<Long> ids);

    /**
     * @param dto      系统操作日志搜索传参
     * @description: 导出系统操作日志excel
     * @author: 白剑民
     * @date: 2022-11-24 13:51:37
     * @version: 1.0
     */
    void exportExcel(LogSearchDTO dto);

    /**
     * @description: 清空日志
     * @author: 白剑民
     * @date: 2023-05-06 10:16:08
     * @version: 1.0
     */
    void clean(LogTypeEnum logType);

    /**
     * @param dto 入参
     * @description: 在线用户页面
     * @author: 白剑民
     * @date: 2023-05-31 12:52:07
     * @return: PageResultVO<OnlineSearchResultVO>
     * @version: 1.0
     */
    PageResultVO<OnlineSearchResultVO> onlineUserPage(OnlineSearchDTO dto);

}
