package cc.jfire.starter.jsql;

import cc.jfire.jsql.SessionFactory;
import cc.jfire.jsql.metadata.TableEntityInfo;
import cc.jfire.jsql.model.Model;

import java.util.Collection;
import java.util.List;

public class ReadOnlySession extends SqlSessionProxy
{
    public ReadOnlySession(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    @Override
    public <T> int save(T entity)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> int update(T entity)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> int insert(T entity)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void batchInsert(Collection<T> list, int batchSize)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int execute(Model model)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int execute(String s, List<Object> list)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String insertReturnPk(String sql, List<Object> params, TableEntityInfo.ColumnInfo pkInfo)
    {
        throw new UnsupportedOperationException();
    }
}
