package com.gientech.iot.test.biz.entity;

import com.gientech.iot.database.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
