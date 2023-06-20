package com.jfirer.starter.jsql.entity;

import com.jfirer.jsql.annotation.AutoIncrement;
import com.jfirer.jsql.annotation.Pk;
import com.jfirer.jsql.annotation.TableDef;

@TableDef("user")
public class User
{
    @Pk
    @AutoIncrement
    private Integer id;
    private String  name;
    private int     age;

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
