package cc.jfire.starter.jsql;

import cc.jfire.baseutil.Resource;
import cc.jfire.jfire.core.aop.impl.support.transaction.AwareJdbcTransactionDatasource;
import cc.jfire.jfire.core.aop.impl.support.transaction.JdbcTransactionManager;
import cc.jfire.jfire.core.aop.impl.support.transaction.JdbcTransactionState;
import cc.jfire.jfire.core.aop.impl.support.transaction.Propagation;
import cc.jfire.jfire.core.inject.notated.CanBeNull;
import cc.jfire.jfire.core.prepare.annotation.condition.provide.ConditionOnMissBeanType;
import cc.jfire.jfire.core.prepare.annotation.configuration.Bean;
import cc.jfire.jfire.core.prepare.annotation.configuration.Configuration;
import cc.jfire.jsql.SessionFactory;
import cc.jfire.jsql.SessionFactoryConfig;
import cc.jfire.jsql.dialect.Dialect;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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
        try
        {
            SessionFactoryConfig sessionfactoryConfig = new SessionFactoryConfig();
            Connection           connection;
            if (dataSource instanceof AwareJdbcTransactionDatasource awareJdbcTransactionDatasource)
            {
                connection = awareJdbcTransactionDatasource.getDataSource().getConnection();
                sessionfactoryConfig.setDataSource(dataSource);
            }
            else
            {
                connection = dataSource.getConnection();
                sessionfactoryConfig.setDataSource(new AwareJdbcTransactionDatasource(dataSource));
            }
            JdbcTransactionManager.CONTEXT.set(new JdbcTransactionState(null, Propagation.REQUIRED, connection));
            sessionfactoryConfig.setDialect(dialect);
            SessionFactory sessionFactory = sessionfactoryConfig.build();
            connection.close();
            JdbcTransactionManager.CONTEXT.remove();
            return sessionFactory;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
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
