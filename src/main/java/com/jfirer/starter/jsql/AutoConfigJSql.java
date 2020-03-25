package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.inject.notated.CanBeNull;
import com.jfirer.jfire.core.inject.notated.PropertyRead;
import com.jfirer.jfire.core.prepare.annotation.condition.provide.ConditionOnMissBeanType;
import com.jfirer.jfire.core.prepare.annotation.configuration.Bean;
import com.jfirer.jfire.core.prepare.annotation.configuration.Configuration;
import com.jfirer.jsql.SessionFactory;
import com.jfirer.jsql.SessionfactoryConfig;
import com.jfirer.jsql.dialect.Dialect;
import com.jfirer.jsql.metadata.TableMode;
import com.jfirer.jsql.session.SqlSession;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class AutoConfigJSql
{
    @Resource
    private DataSource dataSource;
    @Resource
    @CanBeNull
    private Dialect    dialect;
    @PropertyRead("starter.jsql.scanPackage")
    private String     scanPackage;

    @PropertyRead("starter.jsql.tableMode")
    private String tableMode = "none";

    @Bean
    @ConditionOnMissBeanType(SessionFactory.class)
    public SessionFactory sessionFactory()
    {
        SessionfactoryConfig sessionfactoryConfig = new SessionfactoryConfig();
        sessionfactoryConfig.setDataSource(dataSource);
        sessionfactoryConfig.setDialect(dialect);
        sessionfactoryConfig.setScanPackage(scanPackage);
        sessionfactoryConfig.setTableMode(TableMode.valueOf(tableMode.toUpperCase()));
        return sessionfactoryConfig.build();
    }

    @Bean
    public JsqlTransactionManager transactionManager(SessionFactory sessionFactory)
    {
        JsqlTransactionManager jsqlTransactionManager = new JsqlTransactionManager(sessionFactory);
        return jsqlTransactionManager;
    }

    @Bean
    public SqlSession sqlSession(JsqlTransactionManager transactionManager)
    {
        return new SqlSessionProxy(transactionManager);
    }
}
