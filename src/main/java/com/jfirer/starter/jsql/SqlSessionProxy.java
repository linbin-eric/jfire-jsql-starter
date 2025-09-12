package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.prepare.annotation.configuration.Primary;
import com.jfirer.jsql.SessionFactory;
import com.jfirer.jsql.metadata.Page;
import com.jfirer.jsql.metadata.TableEntityInfo;
import com.jfirer.jsql.model.Model;
import com.jfirer.jsql.model.model.QueryModel;
import com.jfirer.jsql.session.SqlSession;
import com.jfirer.jsql.transfer.ResultSetTransfer;
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
        return sessionFactory.openSession().getMapper(mapperClass);
    }

    @Override
    public <T> void batchInsert(Collection<T> collection, int batchSize)
    {
    }

    @Override
    public <T> T findOne(QueryModel model)
    {
        return null;
    }

    @Override
    public <T> List<T> findList(QueryModel model)
    {
        return List.of();
    }

    @Override
    public Page findListByPage(QueryModel model)
    {
        return null;
    }

    @Override
    public int count(Model model)
    {
        return 0;
    }

    @Override
    public int execute(Model model)
    {
        return 0;
    }

    @Override
    public <T> int save(T entity)
    {
        return 0;
    }

    @Override
    public <T> int update(T entity)
    {
        return 0;
    }

    @Override
    public <T> int insert(T entity)
    {
        return 0;
    }

    @Override
    public int execute(String sql, List<Object> params)
    {
        return 0;
    }

    @Override
    public String insertReturnPk(String sql, List<Object> params, TableEntityInfo.ColumnInfo pkInfo)
    {
        return "";
    }

    @Override
    public <T> T query(String sql, ResultSetTransfer transfer, List<Object> params)
    {
        return null;
    }

    @Override
    public <T> List<T> queryList(String sql, ResultSetTransfer transfer, List<Object> params)
    {
        return List.of();
    }

    @Override
    public void close()
    {
        throw new UnsupportedOperationException("由事务管理器管理，不需要手动关闭");
    }

    @Override
    public Connection getConnection()
    {
        return null;
    }
}