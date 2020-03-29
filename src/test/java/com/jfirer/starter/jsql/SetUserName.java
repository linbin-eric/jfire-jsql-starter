package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.aop.notated.Transactional;
import com.jfirer.jsql.model.Model;
import com.jfirer.jsql.session.SqlSession;
import com.jfirer.starter.jsql.entity.User;
import com.jfirer.starter.jsql.mapper.UserOp;
import com.jfirer.starter.jsql.mapper.UserOp2;

import javax.annotation.Resource;

@Resource
public class SetUserName
{
    @Resource
    private SqlSession session;
    @Resource
    private UserOp2    userOp2;

    @Transactional
    public void setUser(int age)
    {
        User user = new User();
        user.setName("xx");
        user.setAge(age);
        session.save(user);
    }

    @Transactional
    public User getUser()
    {
        Model model = Model.query(User.class).where("name", "xx");
        return session.findOne(model);
    }

    @Transactional
    public User getUserByMapper(int id)
    {
        UserOp userOp = session.getMapper(UserOp.class);
        return userOp.find(id);
    }

    @Transactional
    public User getUserByAutoMapper(int id)
    {
        return userOp2.find(id);
    }
}
