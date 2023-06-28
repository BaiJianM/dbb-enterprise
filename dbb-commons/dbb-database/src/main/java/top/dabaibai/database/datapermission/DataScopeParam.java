package top.dabaibai.database.datapermission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 临时存储sql过滤时所用参数
 * @author: 白剑民
 * @dateTime: 2023/4/6 10:34
 */
@Slf4j
@Data
@AllArgsConstructor
public class DataScopeParam implements Serializable {

    private static final long serialVersionUID = 8039329532641391848L;

    /**
     * 默认主表字段过滤(默认以企业id过滤)
     */
    private String defaultField;

    /**
     * 企业数据范围
     */
    private Set<Long> enterpriseIdList;

    private Map<String, String> filterFields;

    /**
     * 是否进行拦截
     */
    private boolean isFilter;

    /**
     * 忽略不过滤的表名
     */
    private List<String> ignoreTables;
}
