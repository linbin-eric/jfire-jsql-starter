package cc.jfire.starter.jsql.mapper;

import cc.jfire.jsql.annotation.Sql;
import cc.jfire.starter.jsql.AutoMapper;
import cc.jfire.starter.jsql.entity.User;

@AutoMapper()
public interface UserOp2
{
    @Sql(sql = "select * from User where id =${id}", paramNames = "id")
    User find(int id);
}
