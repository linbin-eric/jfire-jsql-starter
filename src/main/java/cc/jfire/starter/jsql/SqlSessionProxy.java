package cc.jfire.starter.jsql;

import cc.jfire.jfire.core.aop.impl.support.transaction.JdbcTransactionManager;
import cc.jfire.jfire.core.prepare.annotation.configuration.Primary;
import cc.jfire.jsql.SessionFactory;
import cc.jfire.jsql.metadata.Page;
import cc.jfire.jsql.metadata.TableEntityInfo;
import cc.jfire.jsql.model.Model;
import cc.jfire.jsql.model.model.QueryModel;
import cc.jfire.jsql.session.SqlSession;
import cc.jfire.jsql.transfer.ResultSetTransfer;
import lombok.Data;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

@Data
@Primary
public class SqlSessionProxy implements SqlSession
{
    private final SessionFactory sessionFactory;

    @Override
    public <T> T getMapper(Class<T> mapperClass)
    {
        requireTransactional();
        return sessionFactory.openSession().getMapper(mapperClass);
    }

    private static void requireTransactional()
    {
        if (JdbcTransactionManager.CONTEXT.get() == null)
        {
            throw new RuntimeException("请先开启事务");
        }
    }

    @Override
    public <T> void batchInsert(Collection<T> collection, int batchSize)
    {
        requireTransactional();
        sessionFactory.openSession().batchInsert(collection, batchSize);
    }

    @Override
    public <T> T findOne(QueryModel model)
    {
        requireTransactional();
        return sessionFactory.openSession().findOne(model);
    }

    @Override
    public <T> List<T> findList(QueryModel model)
    {
        requireTransactional();
        return sessionFactory.openSession().findList(model);
    }

    @Override
    public Page findListByPage(QueryModel model)
    {
        requireTransactional();
        return sessionFactory.openSession().findListByPage(model);
    }

    @Override
    public int count(Model model)
    {
        requireTransactional();
        return sessionFactory.openSession().count(model);
    }

    @Override
    public int execute(Model model)
    {
        requireTransactional();
        return sessionFactory.openSession().execute(model);
    }

    @Override
    public <T> int save(T entity)
    {
        requireTransactional();
        return sessionFactory.openSession().save(entity);
    }

    @Override
    public <T> int update(T entity)
    {
        requireTransactional();
        return sessionFactory.openSession().update(entity);
    }

    @Override
    public <T> int insert(T entity)
    {
        requireTransactional();
        return sessionFactory.openSession().insert(entity);
    }

    @Override
    public int execute(String sql, List<Object> params)
    {
        requireTransactional();
        return sessionFactory.openSession().execute(sql, params);
    }

    @Override
    public String insertReturnPk(String sql, List<Object> params, TableEntityInfo.ColumnInfo pkInfo)
    {
        requireTransactional();
        return sessionFactory.openSession().insertReturnPk(sql, params, pkInfo);
    }

    @Override
    public <T> T query(String sql, ResultSetTransfer transfer, List<Object> params)
    {
        requireTransactional();
        return sessionFactory.openSession().query(sql, transfer, params);
    }

    @Override
    public <T> List<T> queryList(String sql, ResultSetTransfer transfer, List<Object> params)
    {
        requireTransactional();
        return sessionFactory.openSession().queryList(sql, transfer, params);
    }

    @Override
    public void close()
    {
        throw new UnsupportedOperationException("由事务管理器管理，不需要手动关闭");
    }

    @Override
    public Connection getConnection()
    {
        requireTransactional();
        return sessionFactory.openSession().getConnection();
    }
}