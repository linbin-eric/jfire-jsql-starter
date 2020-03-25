package com.jfirer.starter.jsql.mapper;

import com.jfirer.jsql.annotation.Sql;
import com.jfirer.starter.jsql.entity.User;

public interface UserOp
{
    @Sql(sql = "select * from User where id =${id}", paramNames = "id")
    User find(int id);
}
