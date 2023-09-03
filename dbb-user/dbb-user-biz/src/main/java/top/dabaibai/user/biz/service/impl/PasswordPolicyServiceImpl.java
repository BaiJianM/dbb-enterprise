package top.dabaibai.user.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dabaibai.core.utils.BeanConvertUtils;
import top.dabaibai.user.api.pojo.dto.PasswordPolicyDTO;
import top.dabaibai.user.biz.entity.SysPasswordPolicy;
import top.dabaibai.user.biz.mapper.PasswordPolicyMapper;
import top.dabaibai.user.biz.service.PasswordPolicyService;

import java.util.List;
import java.util.Optional;

/**
 * @description: 密码策略信息实现类
 * @author: 白剑民
 * @dateTime: 2022/10/24 10:16
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PasswordPolicyServiceImpl extends ServiceImpl<PasswordPolicyMapper, SysPasswordPolicy> implements PasswordPolicyService {

    private final PasswordPolicyMapper policyMapper;

    @Override
    public SysPasswordPolicy getPasswordPolicyById(Long passwordPolicyId) {
        return policyMapper.selectById(passwordPolicyId);
    }

    @Override
    public SysPasswordPolicy getPolicyByEnterpriseId(Long enterpriseId) {
        return policyMapper.getPolicyByEnterpriseId(enterpriseId);
    }

    @Override
    public List<SysPasswordPolicy> getPolicyList() {
        LambdaQueryWrapper<SysPasswordPolicy> wrapper = new LambdaQueryWrapper<>();
        // 获取有效密码策略列表
        wrapper.eq(SysPasswordPolicy::getIsDelete, 0);
        return policyMapper.selectList(wrapper);
    }

    @Override
    public void update(PasswordPolicyDTO dto) {
        // 将传参字段转换赋值成密码策略实体属性
        Optional<SysPasswordPolicy> convert =
                BeanConvertUtils.convert(dto, SysPasswordPolicy::new, (s, t) -> t.setId(s.getPasswordPolicyId()));
        convert.ifPresent(policyMapper::updateById);
    }
}
