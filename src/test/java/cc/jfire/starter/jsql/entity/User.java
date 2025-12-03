package cc.jfire.starter.jsql.entity;

import cc.jfire.jsql.annotation.AutoIncrement;
import cc.jfire.jsql.annotation.Pk;
import cc.jfire.jsql.annotation.TableDef;

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
