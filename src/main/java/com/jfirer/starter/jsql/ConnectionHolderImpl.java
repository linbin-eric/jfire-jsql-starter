package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.aop.impl.transaction.ConnectionHolder;
import com.jfirer.jsql.session.SqlSession;

public class ConnectionHolderImpl implements ConnectionHolder
{
    private final SqlSession       session;
    private       boolean          closed            = false;

    public ConnectionHolderImpl(SqlSession session)
    {
        this.session = session;
    }

    public SqlSession getSession()
    {
        return session;
    }

    @Override
    public void beginTransaction()
    {
        session.beginTransAction();
    }


    @Override
    public void commit()
    {
        session.commit();
    }

    @Override
    public void rollback()
    {
        session.rollback();
    }

    @Override
    public void close()
    {
        closed = true;
        session.close();
    }

    @Override
    public boolean isClosed()
    {
        return closed;
    }

}
