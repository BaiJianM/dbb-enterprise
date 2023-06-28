package com.gientech.iot.demo.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gientech.iot.database.annotation.PessimisticLockInterceptor;
import com.gientech.iot.demo.api.pojo.dto.TestOperationLogDTO;
import com.gientech.iot.demo.biz.entity.DoTest;
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
