package top.dabaibai.user.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.user.api.pojo.dto.EnterpriseRegisterDTO;
import top.dabaibai.user.api.pojo.dto.EnterpriseSearchDTO;
import top.dabaibai.user.api.pojo.dto.EnterpriseUpdateDTO;
import top.dabaibai.user.api.pojo.vo.DepartmentSearchResultVO;
import top.dabaibai.user.api.pojo.vo.EnterpriseCountVO;
import top.dabaibai.user.api.pojo.vo.EnterpriseRegisterResultVO;
import top.dabaibai.user.api.pojo.vo.EnterpriseSearchResultVO;
import top.dabaibai.user.biz.entity.SysEnterprise;
import top.dabaibai.user.biz.enums.CustomErrorCodeEnum;
import top.dabaibai.user.biz.mapper.EnterpriseMapper;
import top.dabaibai.user.biz.service.DepartmentService;
import top.dabaibai.user.biz.service.DepartmentUserService;
import top.dabaibai.user.biz.service.EnterpriseService;
import top.dabaibai.web.commons.http.DbbException;
import top.dabaibai.web.commons.model.PageResultVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 企业/机构信息实现类
 * @author: 白剑民
 * @dateTime: 2022/10/17 17:37
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class EnterpriseServiceImpl extends ServiceImpl<EnterpriseMapper, SysEnterprise> implements EnterpriseService {

    private final EnterpriseMapper enterpriseMapper;

    private final DepartmentService departmentService;

    private final DepartmentUserService departmentUserService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public EnterpriseRegisterResultVO register(EnterpriseRegisterDTO dto) {
        // 将传参字段转换赋值成企业/机构实体属性
        SysEnterprise sysEnterprise = BeanConvertUtils.convert(dto, SysEnterprise::new)
                .orElse(new SysEnterprise());
        enterpriseMapper.insert(sysEnterprise);
        // 封装返回内容
        EnterpriseRegisterResultVO resultVO = new EnterpriseRegisterResultVO();
        resultVO.setEnterpriseId(sysEnterprise.getId());
        return resultVO;
    }

    @Override
    public void update(EnterpriseUpdateDTO dto) {
        // 将传参字段转换赋值成企业/机构实体属性
        SysEnterprise sysEnterprise =
                BeanConvertUtils.convert(dto, SysEnterprise::new, (s, t) -> t.setId(s.getEnterpriseId()))
                        .orElseThrow(() -> new DbbException(CustomErrorCodeEnum.ENTERPRISE_UPDATE_ERROR));
        enterpriseMapper.updateById(sysEnterprise);
    }

    @Override
    public void delete(Long enterpriseId) {
        // 根据企业/机构id获取实体类信息
        SysEnterprise e = enterpriseMapper.selectById(enterpriseId);
        // 判断企业/机构是否存在部门绑定
        List<DepartmentSearchResultVO> deptList = departmentService.getListByEnterpriseId(enterpriseId);
        if (deptList.size() > 0) {
            // 如果存在则抛出异常
            throw new DbbException(CustomErrorCodeEnum.ENTERPRISE_HAS_DEPARTMENT);
        }
        // 将企业/机构信息置为逻辑已删除
        e.setIsDelete(true);
        enterpriseMapper.updateById(e);
    }

    @Override
    public SysEnterprise getEnterpriseInfoById(Long enterpriseId) {
        return enterpriseMapper.selectById(enterpriseId);
    }

    @Override
    public SysEnterprise getEnterpriseInfoByUniqueCode(String uniqueCode) {
        LambdaQueryWrapper<SysEnterprise> wrapper = new LambdaQueryWrapper<>();
        // 匹配社会统一信用代码
        wrapper.eq(SysEnterprise::getUniqueCode, uniqueCode);
        wrapper.eq(SysEnterprise::getIsDelete, 0);
        return enterpriseMapper.selectOne(wrapper);
    }

    @Override
    public PageResultVO<EnterpriseSearchResultVO> search(EnterpriseSearchDTO dto) {
        IPage<EnterpriseSearchResultVO> enterprisePages =
                enterpriseMapper.searchByParam(new Page<>(dto.getCurrent(), dto.getSize()), dto);
        return BeanConvertUtils.convert(enterprisePages, PageResultVO<EnterpriseSearchResultVO>::new)
                .orElse(new PageResultVO<>());
    }

    @Override
    public long count() {
        return enterpriseMapper.selectCount(Wrappers.emptyWrapper());
    }

    @Override
    public EnterpriseCountVO countDepartmentAndUser(Long enterpriseId) {
        EnterpriseCountVO vo = new EnterpriseCountVO();
        // 根据企业/机构id获取部门列表
        List<DepartmentSearchResultVO> deptList = departmentService.getListByEnterpriseId(enterpriseId);
        List<Long> deptIds = deptList.stream().map(DepartmentSearchResultVO::getDepartmentId).collect(Collectors.toList());
        // 根据部门id获取用户id列表
        List<Long> userIdList = departmentUserService.getUserIdsByDeptIds(deptIds);
        // 总用户数量
        vo.setTotalUser(userIdList.size());
        // 总部门数量
        vo.setTotalDepartment(deptList.size());
        return vo;
    }

}
