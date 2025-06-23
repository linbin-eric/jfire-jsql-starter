package com.jfirer.starter.jsql;

import com.jfirer.baseutil.Resource;
import com.jfirer.jfire.core.inject.notated.CanBeNull;
import com.jfirer.jfire.core.prepare.annotation.condition.provide.ConditionOnMissBeanType;
import com.jfirer.jfire.core.prepare.annotation.configuration.Bean;
import com.jfirer.jfire.core.prepare.annotation.configuration.Configuration;
import com.jfirer.jsql.SessionFactory;
import com.jfirer.jsql.SessionFactoryConfig;
import com.jfirer.jsql.dialect.Dialect;

import javax.sql.DataSource;

@Configuration
public class AutoConfigJSql
{
    @Resource
    private DataSource dataSource;
    @Resource
    @CanBeNull
    private Dialect    dialect;

    @Bean
    @ConditionOnMissBeanType(SessionFactory.class)
    public SessionFactory sessionFactory()
    {
        SessionFactoryConfig sessionfactoryConfig = new SessionFactoryConfig();
        sessionfactoryConfig.setDataSource(dataSource);
        sessionfactoryConfig.setDialect(dialect);
        return sessionfactoryConfig.build();
    }

    @Bean
    public JsqlTransactionManager transactionManager(SessionFactory sessionFactory)
    {
        JsqlTransactionManager jsqlTransactionManager = new JsqlTransactionManager(sessionFactory);
        return jsqlTransactionManager;
    }

    @Bean
    public SqlSessionProxy sqlSession(JsqlTransactionManager transactionManager)
    {
        return new SqlSessionProxy(transactionManager);
    }

    @Bean
    public ReadOnlySession readOnlySession(JsqlTransactionManager transactionManager)
    {
        return new ReadOnlySession(transactionManager);
    }

    @Bean
    public MapperFactory mapperFactory(JsqlTransactionManager jsqlTransactionManager)
    {
        return new MapperFactory(jsqlTransactionManager);
    }
}
