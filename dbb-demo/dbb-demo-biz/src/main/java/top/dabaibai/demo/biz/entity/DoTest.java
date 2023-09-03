package top.dabaibai.demo.biz.entity;

import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.dabaibai.database.entity.BaseEntity;
import top.dabaibai.demo.api.pojo.dto.DoTestDTO;
import top.dabaibai.demo.api.pojo.vo.DoTestVO;

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
