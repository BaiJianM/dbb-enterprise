package com.gientech.iot.database.properties;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.gientech.iot.database.enums.DataSourceType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: mybatis配置
 * @author: 白剑民
 * @dateTime: 2023-04-03 09:22:32
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DynamicDataSourceConfiguration {

    /**
     * 读写分离配置(是否开启)
     */
    private Boolean readWrite;

    /**
     * 动态数据源
     */
    private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();

    public Boolean containsSource(DataSourceType sourceType) {
        AtomicReference<Boolean> contains = new AtomicReference<>(Boolean.FALSE);
        datasource.forEach((k, v) -> contains.set(k.startsWith(sourceType.toString().toLowerCase())));
        return contains.get();
    }
}
