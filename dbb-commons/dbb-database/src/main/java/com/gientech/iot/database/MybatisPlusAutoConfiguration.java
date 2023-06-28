package com.gientech.iot.database;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.gientech.iot.database.properties.DynamicDataSourceConfiguration;
import com.gientech.iot.database.datapermission.DataPermissionInterceptor;
import com.gientech.iot.database.annotation.DataScope;
import com.gientech.iot.database.datapermission.DataScopeAnnotationAdvisor;
import com.gientech.iot.database.datapermission.DataScopeAnnotationIntercept;
import com.gientech.iot.database.enums.DataSourceType;
import com.gientech.iot.database.handler.FillMetaObjectHandler;
import com.gientech.iot.database.interceptor.ForUpdateInterceptor;
import com.gientech.iot.database.interceptor.ReadWriteInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.aop.Advisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

/**
 * @description: mybatis-plus配置类
 * @author: 白剑民
 * @dateTime: 2022/7/8 16:34
 */
@Configuration
@EnableConfigurationProperties({DynamicDataSourceConfiguration.class})
@EnableTransactionManagement
public class MybatisPlusAutoConfiguration {

    /**
     * @description: 乐观锁、排他（悲观）锁、分页插件配置
     * @author: 白剑民
     * @date: 2022-07-08 16:37:34
     * @return: com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @version: 1.0
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁仅支持 updateById(id) 与 update(entity, wrapper) 方法，
        // 且在 update(entity, wrapper) 方法下, wrapper 不能复用
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(new ForUpdateInterceptor());
        return interceptor;
    }

    /**
     * @param mybatisConfiguration  自定义多数据源配置
     * @param sqlSessionFactoryList sqlSession工厂列表
     * @description: 当配置了多数据源的时候向每一个sqlSession工厂里添加自定义拦截器
     * @author: 白剑民
     * @date: 2023-04-06 14:28:02
     * @return: java.util.List<org.apache.ibatis.session.SqlSessionFactory>
     * @version: 1.0
     */
    @Bean
    @ConditionalOnBean(DynamicDataSourceConfiguration.class)
    public List<SqlSessionFactory> registerDynamicDatasource(DynamicDataSourceConfiguration mybatisConfiguration,
                                                             List<SqlSessionFactory> sqlSessionFactoryList) {
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            // 读写分离拦截器 需要配置开启 并且 配置了slave数据源 才会生效
            if (mybatisConfiguration.getReadWrite() && mybatisConfiguration.containsSource(DataSourceType.SLAVE)) {
                configuration.addInterceptor(new ReadWriteInterceptor());
            }
        }
        return sqlSessionFactoryList;
    }

    /**
     * @description: 注入数据权限拦截器
     * @author: 白剑民
     * @date: 2023-04-07 13:46:36
     * @return: org.apache.ibatis.plugin.Interceptor
     * @version: 1.0
     */
    @Bean
    public Interceptor registerDataScope() {
        return new DataPermissionInterceptor();
    }

    /**
     * @description: 数据权限拦截配置
     * @author: 白剑民
     * @date: 2023-04-06 11:05:36
     * @return: org.springframework.aop.Advisor
     * @version: 1.0
     */
    @Bean
    public Advisor generateAllDataScopeAdvisor() {
        DataScopeAnnotationIntercept intercept = new DataScopeAnnotationIntercept();
        DataScopeAnnotationAdvisor advisor = new DataScopeAnnotationAdvisor(intercept, DataScope.class);
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return advisor;
    }

    /**
     * @description: 实体类字段自动填充处理器
     * @author: 白剑民
     * @date: 2023-04-27 16:16:36
     * @return: com.gientech.iot.database.handler.FillMetaObjectHandler
     * @version: 1.0
     */
    @Bean
    public FillMetaObjectHandler fillMetaObjectHandler() {
        return new FillMetaObjectHandler();
    }
}
