package top.dabaibai.test.biz.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.dabaibai.database.entity.BaseEntity;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/6/2 14:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogicTest extends BaseEntity {
    private String param_1;
    private String param_2;
}
