package cc.jfire.starter.jsql;

import cc.jfire.baseutil.Resource;
import cc.jfire.jfire.core.aop.notated.Transactional;
import cc.jfire.jsql.model.Model;
import cc.jfire.jsql.model.Param;
import cc.jfire.jsql.session.SqlSession;
import cc.jfire.starter.jsql.entity.User;
import cc.jfire.starter.jsql.mapper.UserOp;
import cc.jfire.starter.jsql.mapper.UserOp2;


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
        return session.findOne(Model.selectAll().from(User.class).where(Param.eq(User::getName, "xx")));
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
