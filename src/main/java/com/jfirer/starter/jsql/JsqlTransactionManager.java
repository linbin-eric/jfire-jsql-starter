package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.aop.impl.support.transaction.ConnectionHolder;
import com.jfirer.jfire.core.aop.impl.support.transaction.JdbcTransactionManager;
import com.jfirer.jfire.core.aop.impl.support.transaction.JdbcTransactionState;
import com.jfirer.jsql.SessionFactory;
import com.jfirer.jsql.session.SqlSession;

import java.util.Objects;

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
        JdbcTransactionState state = CONTEXT.get();
        Objects.requireNonNull(state,"当前不存在事务状态对象，请检查方法是否有事务注解或是否在同一个类的非事务方法调用了事务方法");
        return ((ConnectionHolderImpl) state.getConnectionHolder()).getSession();
    }
}
