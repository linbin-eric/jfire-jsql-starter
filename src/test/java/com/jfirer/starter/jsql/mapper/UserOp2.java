package com.jfirer.starter.jsql.mapper;

import com.jfirer.jsql.annotation.Sql;
import com.jfirer.starter.jsql.AutoMapper;
import com.jfirer.starter.jsql.entity.User;

@AutoMapper()
public interface UserOp2
{
    @Sql(sql = "select * from User where id =${id}", paramNames = "id")
    User find(int id);
}
