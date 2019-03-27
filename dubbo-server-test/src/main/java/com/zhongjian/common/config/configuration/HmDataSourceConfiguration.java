package com.zhongjian.common.config.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Author: ldd
 */
@Configuration
@MapperScan(value = "com.zhongjian.dao.entity.hm", sqlSessionFactoryRef = "hmSqlSessionFactory")
public class HmDataSourceConfiguration {

    @Bean(name = "hmDruidDataSource")
    @ConfigurationProperties(prefix = "druid.datasource")
    public DataSource hmDruidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Bean(name = "hmTransactionManager")
    public DataSourceTransactionManager hmTransactionManager() {
        return new DataSourceTransactionManager(hmDruidDataSource());
    }

    @Bean(name = "hmSqlSessionFactory")
    public SqlSessionFactory hmSqlSessionFactory(@Qualifier("hmDruidDataSource") DataSource masterDataSource
    )
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:com/zhongjian/dao/mapping/hm/*.xml"));
        return sessionFactory.getObject();
    }
}
