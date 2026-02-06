package cc.jfire.starter.jsql;

import cc.jfire.baseutil.reflect.ReflectUtil;
import cc.jfire.baseutil.smc.SmcHelper;
import cc.jfire.baseutil.smc.compiler.CompileHelper;
import cc.jfire.baseutil.smc.model.ClassModel;
import cc.jfire.baseutil.smc.model.MethodModel;
import cc.jfire.jfire.core.bean.BeanDefinition;
import cc.jfire.jfire.core.beanfactory.BeanFactory;
import cc.jfire.jsql.SessionFactory;
import cc.jfire.jsql.session.SqlSession;
import lombok.Data;

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
        String   referenceName = SmcHelper.getReferenceName(descriptorClass, classModel);
        Class<?> type          = descriptorClass;
        while (type != Object.class)
        {
            for (Method each : type.getDeclaredMethods())
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
            type = type.getSuperclass();
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
