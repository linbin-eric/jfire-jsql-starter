package com.jfirer.starter.jsql;

import com.jfirer.jsql.curd.LockMode;
import com.jfirer.jsql.model.Model;
import com.jfirer.jsql.session.SqlSession;
import com.jfirer.jsql.transfer.resultset.ResultSetTransfer;

import java.sql.Connection;
import java.util.List;

public class SqlSessionProxy implements SqlSession
{
    private final JsqlTransactionManager transactionManager;

    public SqlSessionProxy(JsqlTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    @Override
    public <T> T getMapper(Class<T> mapperClass)
    {
        SqlSession sqlSession = transactionManager.currentSession();
        return sqlSession.getMapper(mapperClass);
    }

    @Override
    public void close()
    {
        throw new UnsupportedOperationException("使用声明式事务注解，不要自行操作该接口");
    }

    @Override
    public void beginTransAction()
    {
        throw new UnsupportedOperationException("使用声明式事务注解，不要自行操作该接口");
    }

    @Override
    public void commit()
    {
        throw new UnsupportedOperationException("使用声明式事务注解，不要自行操作该接口");
    }

    @Override
    public void flush()
    {
        throw new UnsupportedOperationException("使用声明式事务注解，不要自行操作该接口");
    }

    @Override
    public void rollback()
    {
        throw new UnsupportedOperationException("使用声明式事务注解，不要自行操作该接口");
    }

    @Override
    public Connection getConnection()
    {
        throw new UnsupportedOperationException("使用声明式事务注解，不要自行操作该接口");
    }

    @Override
    public <T> void save(T entity)
    {
        SqlSession session = transactionManager.currentSession();
        session.save(entity);
    }

    @Override
    public <T> void update(T entity)
    {
        SqlSession session = transactionManager.currentSession();
        session.update(entity);
    }

    @Override
    public <T> int delete(Class<T> ckass, Object pk)
    {
        SqlSession session = transactionManager.currentSession();
        return session.delete(ckass, pk);
    }

    @Override
    public <T> void insert(T entity)
    {
        SqlSession session = transactionManager.currentSession();
        session.insert(entity);
    }

    @Override
    public <T> T get(Class<T> entityClass, Object pk)
    {
        SqlSession session = transactionManager.currentSession();
        return session.get(entityClass, pk);
    }

    @Override
    public <T> T get(Class<T> entityClass, Object pk, LockMode mode)
    {
        SqlSession session = transactionManager.currentSession();
        return session.get(entityClass, pk, mode);
    }

    @Override
    public <T> T findOne(Model model)
    {
        SqlSession session = transactionManager.currentSession();
        return session.findOne(model);
    }

    @Override
    public <T> List<T> find(Model model)
    {
        SqlSession session = transactionManager.currentSession();
        return session.find(model);
    }

    @Override
    public int update(Model model)
    {
        SqlSession session = transactionManager.currentSession();
        return session.update(model);
    }

    @Override
    public int delete(Model model)
    {
        SqlSession session = transactionManager.currentSession();
        return session.delete(model);
    }

    @Override
    public int count(Model model)
    {
        SqlSession session = transactionManager.currentSession();
        return session.count(model);
    }

    @Override
    public void insert(Model model)
    {
        SqlSession session = transactionManager.currentSession();
        session.insert(model);
    }

    @Override
    public int update(String sql, List<Object> params)
    {
        SqlSession session = transactionManager.currentSession();
        return session.update(sql, params);
    }

    @Override
    public String insertReturnPk(String sql, List<Object> params)
    {
        SqlSession session = transactionManager.currentSession();
        return session.insertReturnPk(sql, params);
    }

    @Override
    public <T> T query(ResultSetTransfer transfer, String sql, List<Object> params)
    {
        SqlSession session = transactionManager.currentSession();
        return session.query(transfer, sql, params);
    }

    @Override
    public <T> List<T> queryList(ResultSetTransfer transfer, String sql, List<Object> params)
    {
        SqlSession session = transactionManager.currentSession();
        return session.queryList(transfer, sql, params);
    }
}