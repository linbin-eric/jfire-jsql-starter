package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.aop.impl.transaction.ConnectionHolder;
import com.jfirer.jfire.core.aop.impl.transaction.JdbcTransactionManager;
import com.jfirer.jsql.SessionFactory;
import com.jfirer.jsql.session.SqlSession;

public class JsqlTransactionManager extends JdbcTransactionManager
{
    public JsqlTransactionManager(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    private SessionFactory sessionFactory;

    @Override
    protected ConnectionHolder openConnection()
    {
        SqlSession sqlSession = sessionFactory.openSession();
        return new ConnectionHolderImpl(sqlSession);
    }

    public SqlSession currentSession()
    {
        ConnectionHolder connectionHolder = CONTEXT.get();
        return connectionHolder == null ? null : ((ConnectionHolderImpl) connectionHolder).getSession();
    }
}
