package com.jfirer.starter.jsql;

import com.jfirer.baseutil.reflect.ReflectUtil;
import com.jfirer.baseutil.smc.SmcHelper;
import com.jfirer.baseutil.smc.compiler.CompileHelper;
import com.jfirer.baseutil.smc.model.ClassModel;
import com.jfirer.baseutil.smc.model.MethodModel;
import com.jfirer.jfire.core.BeanFactory;
import com.jfirer.jfire.core.beandescriptor.BeanDescriptor;
import com.jfirer.jsql.mapper.Mapper;
import com.jfirer.jsql.session.SqlSession;

import java.lang.reflect.Method;

public class MapperFactory implements BeanFactory
{
    private       CompileHelper          compileHelper = new CompileHelper();
    private final JsqlTransactionManager transactionManager;

    public MapperFactory(JsqlTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    @Override
    public <E> E getBean(BeanDescriptor beanDescriptor)
    {
        Class<?> descriptorClass = beanDescriptor.getDescriptorClass();
        ClassModel classModel    = new ClassModel(descriptorClass.getSimpleName() + "$Mapper", MapperProxy.class, descriptorClass);
        classModel.addImport(SqlSession.class);
        String     referenceName = SmcHelper.getReferenceName(descriptorClass, classModel);
        for (Method each : descriptorClass.getDeclaredMethods())
        {
            MethodModel   methodModel = new MethodModel(each, classModel);
            StringBuilder body        = new StringBuilder();
            body.append("SqlSession sqlSession = transactionManager.currentSession();\r\n");
            body.append("if(sqlSession==null){throw new NullPointerException(\"session 为空\");\r\n}");
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
            mapperProxy.setTransactionManager(transactionManager);
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
        protected JsqlTransactionManager transactionManager;

        public void setTransactionManager(JsqlTransactionManager transactionManager)
        {
            this.transactionManager = transactionManager;
        }
    }
}
