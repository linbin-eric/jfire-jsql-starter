package cc.jfire.starter.jsql;

import cc.jfire.jfire.core.aop.impl.support.transaction.JdbcTransactionManager;
import cc.jfire.jsql.SessionFactory;
import cc.jfire.jsql.metadata.Page;
import cc.jfire.jsql.metadata.TableEntityInfo;
import cc.jfire.jsql.model.Model;
import cc.jfire.jsql.model.model.QueryModel;
import cc.jfire.jsql.session.SqlSession;
import cc.jfire.jsql.transfer.ResultSetTransfer;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

/**
 * 只读Session，只允许执行读取操作，写入操作会抛出异常
 */
public class ReadOnlySession implements SqlSession
{
    private final SessionFactory sessionFactory;

    public ReadOnlySession(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    private static void requireTransactional()
    {
        if (JdbcTransactionManager.CONTEXT.get() == null)
        {
            throw new RuntimeException("请先开启事务");
        }
    }

    private static UnsupportedOperationException readOnlyException()
    {
        return new UnsupportedOperationException("当前为只读Session，不允许执行写入操作");
    }

    @Override
    public <T> T getMapper(Class<T> mapperClass)
    {
        throw readOnlyException();
    }

    @Override
    public <T> void batchInsert(Collection<T> collection, int batchSize)
    {
        throw readOnlyException();
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
        throw readOnlyException();
    }

    @Override
    public <T> int save(T entity)
    {
        throw readOnlyException();
    }

    @Override
    public <T> int update(T entity)
    {
        throw readOnlyException();
    }

    @Override
    public <T> int insert(T entity)
    {
        throw readOnlyException();
    }

    @Override
    public int execute(String sql, List<Object> params)
    {
        throw readOnlyException();
    }

    @Override
    public String insertReturnPk(String sql, List<Object> params, TableEntityInfo.ColumnInfo pkInfo)
    {
        throw readOnlyException();
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
        throw new UnsupportedOperationException("由框架管理，不需要手动关闭");
    }

    @Override
    public Connection getConnection()
    {
        requireTransactional();
        return sessionFactory.openSession().getConnection();
    }
}
