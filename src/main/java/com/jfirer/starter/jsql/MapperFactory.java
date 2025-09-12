package com.jfirer.starter.jsql;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.compiler.CompileHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfire.core.aop.impl.support.transaction.JdbcTransactionManager;
import com.jfirer.jfire.core.bean.BeanDefinition;
import com.jfirer.jfire.core.beanfactory.BeanFactory;
import com.jfirer.jsql.SessionFactory;
import com.jfirer.jsql.mapper.Mapper;
import com.jfirer.jsql.session.SqlSession;
import lombok.Data;

import java.beans.BeanDescriptor;
import java.lang.reflect.Method;

@Data
public class MapperFactory implements BeanFactory
{
    private       CompileHelper  compileHelper = new CompileHelper();
    private final SessionFactory sessionFactory;

    @Override
    public <E> E getUnEnhanceyInstance(BeanDefinition beanDefinition)
    {
        Class<?>   descriptorClass = beanDefinition.getType();
        ClassModel classModel      = new ClassModel(descriptorClass.getSimpleName() + "$Mapper", MapperProxy.class, descriptorClass);
        classModel.addImport(SqlSession.class);
        String referenceName = SmcHelper.getReferenceName(descriptorClass, classModel);
        for (Method each : descriptorClass.getDeclaredMethods())
        {
            MethodModel   methodModel = new MethodModel(each, classModel);
            StringBuilder body        = new StringBuilder();
            body.append("SqlSession sqlSession = sessionFactory.openSession();\r\n");
            if (each.getReturnType() == void.class)
            {
                ;
            }
            else
            {
                body.append("return ");
            }
            body.append("sqlSession.getMapper(").append(referenceName).append(".class).").append(each.getName()).append('(');
            Class<?>[] parameterTypes = each.getParameterTypes();
            boolean    hasComma       = false;
            for (int i = 0; i < parameterTypes.length; i++)
            {
                hasComma = true;
                body.append("$").append(i).append(',');
            }
            if (hasComma)
            {
                body.setLength(body.length() - 1);
            }
            body.append(");\r\n");
            methodModel.setBody(body.toString());
            classModel.putMethodModel(methodModel);
        }
        try
        {
            Class<?>    compile     = compileHelper.compile(classModel);
            MapperProxy mapperProxy = (MapperProxy) compile.newInstance();
            mapperProxy.setSessionFactory(sessionFactory);
            return (E) mapperProxy;
        }
        catch (Exception e)
        {
            ReflectUtil.throwException(e);
            return null;
        }
    }

    public static class MapperProxy
    {
        protected SessionFactory sessionFactory;

        public void setSessionFactory(SessionFactory sessionFactory)
        {
            this.sessionFactory = sessionFactory;
        }
    }
}
