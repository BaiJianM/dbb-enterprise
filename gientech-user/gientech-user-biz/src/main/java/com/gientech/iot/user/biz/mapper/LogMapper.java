package com.gientech.iot.user.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gientech.iot.user.api.pojo.dto.LogSearchDTO;
import com.gientech.iot.user.api.pojo.dto.OnlineSearchDTO;
import com.gientech.iot.user.api.pojo.vo.LogSearchResultVO;
import com.gientech.iot.user.api.pojo.vo.OnlineSearchResultVO;
import com.gientech.iot.user.biz.entity.SysLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * @description: 日志Mapper接口
 * @author: 白剑民
 * @dateTime: 2022-11-21 09:49:30
 */
@Repository
public interface LogMapper extends BaseMapper<SysLog> {

    /**
     * @param page 分页参数
     * @param dto  系统日志搜索传参
     * @description: 系统日志搜索
     * @author: 白剑民
     * @date: 2022-11-21 09:49:34
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.gientech.iot.user.api.entity.vo.LogSearchResultVO>
     * @version: 1.0
     */
    IPage<LogSearchResultVO> search(IPage<LogSearchResultVO> page, @Param("dto") LogSearchDTO dto);

    /**
     * @param eventCodes 操作事件码
     * @param logType    日志类型
     * @param startDate  开始日期
     * @param endDate    截止日期
     * @description: 获取指定事件、时间范围、指定类型的日志
     * @author: 白剑民
     * @date: 2023-05-29 19:41:11
     * @return: java.util.List<com.gientech.iot.user.biz.entity.SysLog>
     * @version: 1.0
     */
    List<SysLog> getLogByParams(@Param("eventCodes") List<String> eventCodes,
                                @Param("logType") String logType,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);


    /**
     * @param page 分页参数
     * @param dto  在线用户搜索传参
     * @description: 在线用户页面
     * @author: 王强
     * @date: 2023-05-31 11:47:04
     * @return: IPage<OnlineSearchResultVO>
     * @version: 1.0
     */
    IPage<OnlineSearchResultVO> onlineUserPage(IPage<OnlineSearchResultVO> page, @Param("dto") OnlineSearchDTO dto);
}
