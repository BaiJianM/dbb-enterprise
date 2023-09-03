package top.dabaibai.user.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dabaibai.core.enums.ExcelTypeEnum;
import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.core.utils.DateUtils;
import top.dabaibai.core.utils.SpringContextUtils;
import top.dabaibai.core.utils.excel.ExcelUtil;
import top.dabaibai.log.core.enums.LogTypeEnum;
import top.dabaibai.log.core.pojo.LogDTO;
import top.dabaibai.log.core.service.ILogService;
import top.dabaibai.user.api.enums.LoginTypeEnum;
import top.dabaibai.user.api.pojo.dto.LogSearchDTO;
import top.dabaibai.user.api.pojo.dto.OnlineSearchDTO;
import top.dabaibai.user.api.pojo.vo.LogSearchResultVO;
import top.dabaibai.user.api.pojo.vo.LoginLogExportResultVO;
import top.dabaibai.user.api.pojo.vo.OnlineSearchResultVO;
import top.dabaibai.user.api.pojo.vo.OperateLogExportResultVO;
import top.dabaibai.user.biz.entity.SysLog;
import top.dabaibai.user.biz.mapper.LogMapper;
import top.dabaibai.user.biz.service.LogService;
import top.dabaibai.web.commons.model.PageResultVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 日志信息实现类
 * @author: 白剑民
 * @dateTime: 2022/11/18 15:05
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class LogServiceImpl extends ServiceImpl<LogMapper, SysLog> implements LogService, ILogService {

    private final LogMapper logMapper;

    @Override
    public void createLog(LogDTO dto) {
        SysLog sysLog = BeanConvertUtils.convert(dto, SysLog::new, (s, t) -> {
            t.setModule(s.getBizType());
            t.setEvent(s.getBizEvent());
            t.setMsg(dto.getMsg());
            t.setExtra(s.getExtra());
            t.setType(Integer.valueOf(s.getTag()));
            t.setCreateTime(LocalDateTime.now());
            t.setCreateUserId(Long.parseLong(s.getOperatorId() == null ? "0" : s.getOperatorId()));
            t.setCreateUserName(s.getOperatorName());
            t.setCreateUserCode(s.getOperatorCode());
        }).orElse(new SysLog());
        logMapper.insert(sysLog);
    }

    @Override
    public PageResultVO<LogSearchResultVO> search(LogSearchDTO dto) {
        IPage<LogSearchResultVO> search = logMapper.search(new Page<>(dto.getCurrent(), dto.getSize()), dto);
        return BeanConvertUtils.convert(search, PageResultVO<LogSearchResultVO>::new).orElse(new PageResultVO<>());
    }

    @Override
    public void delete(List<Long> ids) {
        logMapper.deleteBatchIds(ids);
    }

    @Override
    public void exportExcel(LogSearchDTO dto) {
        PageResultVO<LogSearchResultVO> page = this.search(dto);
        // 序号
        AtomicInteger serialNo = new AtomicInteger(1);
        // 登陆日志导出
        if (LogTypeEnum.LOGIN_LOG == dto.getLogType()) {
            Collection<LoginLogExportResultVO> records = BeanConvertUtils.convertCollection(page.getRecords(), LoginLogExportResultVO::new, (s, t) -> {
                t.setEvent(LoginTypeEnum.getName(s.getEvent()));
                t.setSerialNo(serialNo.getAndIncrement());
            }).orElse(new ArrayList<>());
            ExcelUtil.exportExcel(SpringContextUtils.getResponse(),
                    dto.getLogType().getName() + "-" + DateUtils.parseTime(LocalDateTime.now()),
                    records, LoginLogExportResultVO.class, ExcelTypeEnum.XLSX);
        }
        // 操作日志导出
        else if (LogTypeEnum.OPERATE_LOG == dto.getLogType()) {
            Collection<OperateLogExportResultVO> records = BeanConvertUtils.convertCollection(page.getRecords(), OperateLogExportResultVO::new, (s, t) -> {
                t.setEvent(LoginTypeEnum.getName(s.getEvent()));
                t.setSerialNo(serialNo.getAndIncrement());
            }).orElse(new ArrayList<>());
            ExcelUtil.exportExcel(SpringContextUtils.getResponse(),
                    dto.getLogType().getName() + "-" + DateUtils.parseTime(LocalDateTime.now()),
                    records, OperateLogExportResultVO.class, ExcelTypeEnum.XLSX);
        }
    }

    @Override
    public void clean(LogTypeEnum logType) {
        logMapper.delete(new LambdaQueryWrapper<SysLog>().eq(SysLog::getType, logType.getCode()));
    }

    @Override
    public PageResultVO<OnlineSearchResultVO> onlineUserPage(OnlineSearchDTO dto) {
        dto.setEvent(LoginTypeEnum.LOGIN.getCode());
        dto.setStartCreateTime(LocalDateTime.now().plusHours(-8));
        IPage<OnlineSearchResultVO> search = logMapper.onlineUserPage(new Page<>(dto.getCurrent(), dto.getSize()), dto);
        return BeanConvertUtils.convert(search, PageResultVO<OnlineSearchResultVO>::new).orElse(new PageResultVO<>());
    }
}
