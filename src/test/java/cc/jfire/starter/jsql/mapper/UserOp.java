package cc.jfire.starter.jsql.mapper;

import cc.jfire.jsql.annotation.Sql;
import cc.jfire.jsql.mapper.Mapper;
import cc.jfire.starter.jsql.entity.User;

@Mapper()
public interface UserOp
{
    @Sql(sql = "select * from User where id =${id}", paramNames = "id")
    User find(int id);
}
