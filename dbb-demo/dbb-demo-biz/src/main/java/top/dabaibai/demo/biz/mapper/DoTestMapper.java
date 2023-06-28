package top.dabaibai.demo.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.dabaibai.database.annotation.PessimisticLockInterceptor;
import top.dabaibai.demo.api.pojo.dto.TestOperationLogDTO;
import top.dabaibai.demo.biz.entity.DoTest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoTestMapper extends BaseMapper<DoTest> {

    @Override
    @PessimisticLockInterceptor(forUpdate = true)
    List<DoTest> selectList(Wrapper<DoTest> queryWrapper);

    void testUpdate(@Param("dto") TestOperationLogDTO dto);
}
