package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.prepare.annotation.configuration.Primary;
import com.jfirer.jsql.model.Model;
import com.jfirer.jsql.session.SqlSession;

import java.lang.reflect.AnnotatedElement;
import java.sql.Connection;
import java.util.List;

@Primary
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
        return transactionManager.currentSession().getMapper(mapperClass);
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
    public <T> int save(T entity)
    {
        return transactionManager.currentSession().save(entity);
    }

    @Override
    public <T> int update(T entity)
    {
        return transactionManager.currentSession().update(entity);
    }

    @Override
    public <T> int insert(T entity)
    {
        return transactionManager.currentSession().insert(entity);
    }

    @Override
    public <T> void batchInsert(List<T> list)
    {
        transactionManager.currentSession().batchInsert(list);
    }

    @Override
    public <T> T findOne(Model model)
    {
        return transactionManager.currentSession().findOne(model);
    }

    @Override
    public <T> List<T> findList(Model model)
    {
        return transactionManager.currentSession().findList(model);
    }

    @Override
    public int count(Model model)
    {
        return transactionManager.currentSession().count(model);
    }

    @Override
    public int execute(Model model)
    {
        return transactionManager.currentSession().execute(model);
    }

    @Override
    public int execute(String s, List<Object> list)
    {
        return transactionManager.currentSession().execute(s, list);
    }

    @Override
    public String insertReturnPk(String sql, List<Object> params)
    {
        return transactionManager.currentSession().insertReturnPk(sql, params);
    }

    @Override
    public <T> T query(String s, AnnotatedElement annotatedElement, List<Object> list)
    {
        return transactionManager.currentSession().query(s, annotatedElement, list);
    }

    @Override
    public <T> List<T> queryList(String s, AnnotatedElement annotatedElement, List<Object> list)
    {
        return transactionManager.currentSession().queryList(s, annotatedElement, list);
    }
}