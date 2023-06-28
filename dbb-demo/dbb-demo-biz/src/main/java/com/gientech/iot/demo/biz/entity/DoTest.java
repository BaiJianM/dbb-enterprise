package com.gientech.iot.demo.biz.entity;

import com.gientech.iot.database.entity.BaseEntity;
import com.gientech.iot.demo.api.pojo.dto.DoTestDTO;
import com.gientech.iot.demo.api.pojo.vo.DoTestVO;
import io.github.linpeilie.annotations.AutoMapMapper;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/3/2 14:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMappers({
        @AutoMapper(target = DoTestDTO.class),
        @AutoMapper(target = DoTestVO.class),
})
public class DoTest extends BaseEntity {
    private String name;
}
