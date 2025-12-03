# JFire-JSql-Starter

JFire 框架的 JSql 自动配置启动器，提供开箱即用的数据库访问能力。

## 特性

- 自动配置 `SessionFactory` 和事务管理器
- 支持 `SqlSession` 依赖注入
- 提供 `@AutoMapper` 注解，自动注入 Mapper 接口
- 内置只读会话 `ReadOnlySession`，防止误操作
- 集成 JFire 事务管理

## 环境要求

- Java 17+
- JFire 1.0
- JSql 1.0

## 安装

```xml
<dependency>
    <groupId>cc.jfire</groupId>
    <artifactId>jfire-jsql-starter</artifactId>
    <version>1.0</version>
</dependency>
```

## 快速开始

### 1. 配置数据源

```java
@Configuration
@EnableAutoConfiguration
public class AppConfig {
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        return dataSource;
    }
}
```

### 2. 使用 SqlSession

```java
@Resource
public class UserService {
    @Resource
    private SqlSession session;

    @Transactional
    public void saveUser(User user) {
        session.save(user);
    }

    @Transactional
    public User findUser(String name) {
        return session.findOne(
            Model.selectAll()
                 .from(User.class)
                 .where(Param.eq(User::getName, name))
        );
    }
}
```

### 3. 使用 Mapper 接口

定义 Mapper 接口：

```java
@AutoMapper
public interface UserMapper {
    @Sql(sql = "select * from user where id = ${id}", paramNames = "id")
    User findById(int id);
}
```

注入使用：

```java
@Resource
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Transactional
    public User getUser(int id) {
        return userMapper.findById(id);
    }
}
```

## 核心组件

| 组件 | 说明 |
|------|------|
| `SqlSessionProxy` | SqlSession 代理，自动管理会话生命周期 |
| `ReadOnlySession` | 只读会话，禁止写操作 |
| `MapperFactory` | Mapper 接口工厂，动态生成实现类 |
| `@AutoMapper` | 标记 Mapper 接口，支持自动注入 |

## 许可证

[AGPL-3.0](LICENSE)
