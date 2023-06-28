package com.gientech.iot.user.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gientech.iot.core.utils.BeanConvertUtils;
import com.gientech.iot.user.api.pojo.dto.SystemCreateDTO;
import com.gientech.iot.user.api.pojo.dto.SystemSearchDTO;
import com.gientech.iot.user.api.pojo.dto.SystemUpdateDTO;
import com.gientech.iot.user.api.pojo.vo.SystemCreateResultVO;
import com.gientech.iot.user.api.pojo.vo.SystemDetailResultVO;
import com.gientech.iot.user.api.pojo.vo.SystemSearchResultVO;
import com.gientech.iot.user.biz.entity.SysSystem;
import com.gientech.iot.user.biz.enums.CustomErrorCodeEnum;
import com.gientech.iot.user.biz.mapper.SystemMapper;
import com.gientech.iot.user.biz.service.SystemService;
import com.gientech.iot.web.commons.http.GientechException;
import com.gientech.iot.web.commons.model.PageResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 子系统信息实现类
 * @author: 白剑民
 * @dateTime: 2022/10/31 11:04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class SystemServiceImpl extends ServiceImpl<SystemMapper, SysSystem> implements SystemService {

    private final SystemMapper systemMapper;

    @Override
    public SystemCreateResultVO create(SystemCreateDTO dto) {
        // 将传参字段转换赋值成子系统实体属性
        SysSystem system = BeanConvertUtils.convert(dto, SysSystem::new)
                .orElseThrow(() -> new GientechException(CustomErrorCodeEnum.SYSTEM_CREATE_ERROR));
        systemMapper.insert(system);
        // 封装返回内容
        SystemCreateResultVO resultVO = new SystemCreateResultVO();
        resultVO.setSystemId(system.getId());
        return resultVO;
    }

    @Override
    public SystemDetailResultVO detail(Long systemId) {
        SysSystem system = systemMapper.selectById(systemId);
        return BeanConvertUtils.convert(system, SystemDetailResultVO::new, (s, t) -> {
            t.setSystemId(s.getId());
        }).orElse(new SystemDetailResultVO());
    }

    @Override
    public void update(SystemUpdateDTO dto) {
        // 将传参字段转换赋值成子系统实体属性
        SysSystem system = BeanConvertUtils.convert(dto, SysSystem::new, (s, t) -> t.setId(s.getSystemId())).orElse(new SysSystem());
        // 更新子系统信息
        systemMapper.updateById(system);
    }

    @Override
    public void delete(List<Long> systemIds) {
        // 子系统逻辑删除
        systemMapper.deleteBatchIds(systemIds);
    }

    @Override
    public List<SystemSearchResultVO> list(SystemSearchDTO dto) {
        List<SysSystem> systemList = systemMapper.selectList(new LambdaQueryWrapper<SysSystem>()
                .eq(dto.getIsEnable() != null, SysSystem::getIsEnable, dto.getIsEnable())
                .like(StringUtils.isNotEmpty(dto.getSystemName()), SysSystem::getSystemName, dto.getSystemName())
                .like(StringUtils.isNotEmpty(dto.getServiceId()), SysSystem::getServiceId, dto.getServiceId()));
        return (List<SystemSearchResultVO>) BeanConvertUtils.convertCollection(systemList, SystemSearchResultVO::new, (s, t) -> {
            t.setSystemId(s.getId());
        }).orElse(new ArrayList<>());
    }

    @Override
    public PageResultVO<SystemSearchResultVO> page(SystemSearchDTO dto) {
        IPage<SysSystem> page = systemMapper.selectPage(new Page<>(dto.getCurrent(), dto.getSize()),
                new LambdaQueryWrapper<SysSystem>()
                        .eq(dto.getIsEnable() != null, SysSystem::getIsEnable, dto.getIsEnable())
                        .like(StringUtils.isNotEmpty(dto.getSystemName()), SysSystem::getSystemName, dto.getSystemName())
                        .like(StringUtils.isNotEmpty(dto.getServiceId()), SysSystem::getServiceId, dto.getServiceId()));
        List<SystemSearchResultVO> list = (List<SystemSearchResultVO>) BeanConvertUtils.convertCollection(page.getRecords(), SystemSearchResultVO::new, (s, t) -> {
            t.setSystemId(s.getId());
        }).orElse(new ArrayList<>());
        return new PageResultVO<>(page.getTotal(), page.getSize(), page.getCurrent(), page.getPages(), list);
    }

    @Override
    public void changeStatus(Long systemId, Boolean isEnable) {
        SysSystem system = systemMapper.selectById(systemId);
        system.setIsEnable(isEnable);
        systemMapper.updateById(system);
    }
}
