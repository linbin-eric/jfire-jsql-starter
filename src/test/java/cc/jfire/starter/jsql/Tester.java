package cc.jfire.starter.jsql;

import cc.jfire.jfire.core.ApplicationContext;
import cc.jfire.jfire.core.DefaultApplicationContext;
import cc.jfire.jfire.core.prepare.annotation.AddProperty;
import cc.jfire.jfire.core.prepare.annotation.ComponentScan;
import cc.jfire.jfire.core.prepare.annotation.EnableAutoConfiguration;
import cc.jfire.jfire.core.prepare.annotation.configuration.Bean;
import cc.jfire.jfire.core.prepare.annotation.configuration.Configuration;
import cc.jfire.starter.jsql.entity.User;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    @Before
    public void before() throws SQLException
    {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:test");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        Connection connection = dataSource.getConnection();
        connection.prepareStatement("""
                                    drop table if exists user
                                    """).execute();
        PreparedStatement preparedStatement = connection.prepareStatement("""
                    create table user(                                                      
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NULL,
  `age` int NULL,
  PRIMARY KEY (`id`)                  )                                                        
                                                                          """);
        preparedStatement.execute();
    }

    @Test
    public void test()
    {
        ApplicationContext applicationContext = new DefaultApplicationContext(Tester.class);
        SetUserName        setUserName        = applicationContext.getBean(SetUserName.class);
        setUserName.setUser(12);
        User user = setUserName.getUser();
        Assert.assertEquals(12, user.getAge());
        Assert.assertEquals(12, setUserName.getUserByMapper(user.getId()).getAge());
    }

    @Test
    public void test2()
    {
        ApplicationContext applicationContext = new DefaultApplicationContext(Tester.class);
        SetUserName        setUserName        = applicationContext.getBean(SetUserName.class);
        setUserName.setUser(12);
        User user = setUserName.getUser();
        Assert.assertEquals(12, user.getAge());
        Assert.assertEquals(12, setUserName.getUserByAutoMapper(user.getId()).getAge());
    }
}
