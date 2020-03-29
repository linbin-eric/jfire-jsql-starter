package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.AnnotatedApplicationContext;
import com.jfirer.jfire.core.ApplicationContext;
import com.jfirer.jfire.core.prepare.annotation.AddProperty;
import com.jfirer.jfire.core.prepare.annotation.ComponentScan;
import com.jfirer.jfire.core.prepare.annotation.EnableAutoConfiguration;
import com.jfirer.jfire.core.prepare.annotation.configuration.Bean;
import com.jfirer.jfire.core.prepare.annotation.configuration.Configuration;
import com.jfirer.starter.jsql.entity.User;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.jfirer.starter.jsql")
@AddProperty({"starter.jsql.scanPackage=com.jfirer.starter.jsql.entity;com.jfirer.starter.jsql.mapper", "starter.jsql.tableMode=create"})
public class Tester
{
    @Bean
    public DataSource dataSource()
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:test");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }

    @Test
    public void test()
    {
        ApplicationContext applicationContext = new AnnotatedApplicationContext(Tester.class);
        SetUserName        setUserName        = applicationContext.getBean(SetUserName.class);
        setUserName.setUser(12);
        User user = setUserName.getUser();
        Assert.assertEquals(12, user.getAge());
        Assert.assertEquals(12, setUserName.getUserByMapper(user.getId()).getAge());
    }

    @Test
    public void test2()
    {
        ApplicationContext applicationContext = new AnnotatedApplicationContext(Tester.class);
        SetUserName        setUserName        = applicationContext.getBean(SetUserName.class);
        setUserName.setUser(12);
        User user = setUserName.getUser();
        Assert.assertEquals(12, user.getAge());
        Assert.assertEquals(12, setUserName.getUserByAutoMapper(user.getId()).getAge());
    }
}
