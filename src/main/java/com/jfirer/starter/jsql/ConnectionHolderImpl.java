package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.aop.impl.transaction.ConnectionHolder;
import com.jfirer.jsql.session.SqlSession;

public class ConnectionHolderImpl implements ConnectionHolder
{
    private final SqlSession       session;
    private       boolean          transactionActive = false;
    private       boolean          closed            = false;
    private       ConnectionHolder prev;

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
        transactionActive = true;
    }

    @Override
    public boolean isTransactionActive()
    {
        return transactionActive;
    }

    @Override
    public void commit()
    {
        transactionActive = false;
        session.commit();
    }

    @Override
    public void rollback()
    {
        transactionActive = false;
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

    @Override
    public ConnectionHolder getPrev()
    {
        return prev;
    }

    @Override
    public void setPrev(ConnectionHolder connectionHolder)
    {
        this.prev = connectionHolder;
    }
}
