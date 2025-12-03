package cc.jfire.starter.jsql;

import cc.jfire.baseutil.Resource;
import cc.jfire.jfire.core.aop.impl.support.transaction.AwareJdbcTransactionDatasource;
import cc.jfire.jfire.core.aop.impl.support.transaction.JdbcTransactionManager;
import cc.jfire.jfire.core.inject.notated.CanBeNull;
import cc.jfire.jfire.core.prepare.annotation.condition.provide.ConditionOnMissBeanType;
import cc.jfire.jfire.core.prepare.annotation.configuration.Bean;
import cc.jfire.jfire.core.prepare.annotation.configuration.Configuration;
import cc.jfire.jsql.SessionFactory;
import cc.jfire.jsql.SessionFactoryConfig;
import cc.jfire.jsql.dialect.Dialect;

import javax.sql.DataSource;

@Configuration
public class AutoConfigJSql
{
    @Resource
    @CanBeNull
    private Dialect dialect;

    @Bean
    @ConditionOnMissBeanType(SessionFactory.class)
    public SessionFactory sessionFactory(DataSource dataSource)
    {
        SessionFactoryConfig sessionfactoryConfig = new SessionFactoryConfig();
        if (dataSource instanceof AwareJdbcTransactionDatasource awareJdbcTransactionDatasource)
        {
            sessionfactoryConfig.setDataSource(dataSource);
        }
        else
        {
            sessionfactoryConfig.setDataSource(new AwareJdbcTransactionDatasource(dataSource));
        }
        sessionfactoryConfig.setDialect(dialect);
        return sessionfactoryConfig.build();
    }

    @Bean
    public JdbcTransactionManager transactionManager(DataSource dataSource)
    {
        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionProxy sqlSession(SessionFactory sessionFactory)
    {
        return new SqlSessionProxy(sessionFactory);
    }

    @Bean
    public ReadOnlySession readOnlySession(SessionFactory sessionFactory)
    {
        return new ReadOnlySession(sessionFactory);
    }

    @Bean
    public MapperFactory mapperFactory(SessionFactory sessionFactory)
    {
        return new MapperFactory(sessionFactory);
    }
}
