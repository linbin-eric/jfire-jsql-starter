package cc.jfire.starter.jsql;

import cc.jfire.baseutil.STR;
import cc.jfire.baseutil.reflect.ReflectUtil;
import cc.jfire.baseutil.smc.SmcHelper;
import cc.jfire.baseutil.smc.compiler.CompileHelper;
import cc.jfire.baseutil.smc.model.ClassModel;
import cc.jfire.baseutil.smc.model.MethodModel;
import cc.jfire.jfire.core.aop.impl.support.transaction.JdbcTransactionManager;
import cc.jfire.jfire.core.bean.BeanDefinition;
import cc.jfire.jfire.core.beanfactory.BeanFactory;
import cc.jfire.jsql.SessionFactory;
import cc.jfire.jsql.mapper.Repository;
import cc.jfire.jsql.metadata.Page;
import cc.jfire.jsql.model.Param;
import cc.jfire.jsql.session.SqlSession;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (type.isInterface() == false)
        {
            throw new IllegalArgumentException();
        }
        classModel.addImport(JdbcTransactionManager.class);
        classModel.addImport(ThreadLocal.class);
        for (Method each : type.getMethods())
        {
            if (each.getDeclaringClass() == Repository.class)
            {
                continue;
            }
            MethodModel   methodModel = new MethodModel(each, classModel);
            StringBuilder body        = new StringBuilder();
            body.append("""
                                if (JdbcTransactionManager.CONTEXT.get() == null)
                                        {
                                            throw new RuntimeException("请先开启事务");
                                        }\r\n""");
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
        Set<Class> set = new HashSet<>();
        findAllInterface(type, set);
        if (set.contains(Repository.class))
        {
            Type     genericSuperclass  = findReposositoryInterface(type);
            Type     actualTypeArgument = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
            Class<?> repositoryClass    = (Class<?>) actualTypeArgument;
            addFindOneMethod(classModel, repositoryClass, descriptorClass);
            addFindListMethod(classModel, repositoryClass, descriptorClass);
            addFindListPageMethod(classModel, repositoryClass, descriptorClass);
            addCountMethod(classModel, descriptorClass);
            addDeleteMethod(classModel, descriptorClass);
            addInsertMethod(classModel, repositoryClass, descriptorClass);
            addSaveMethod(classModel, repositoryClass, descriptorClass);
            addUpdateMethod(classModel, repositoryClass, descriptorClass);
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

    private void findAllInterface(Class<?> type, Set<Class> set)
    {
        set.add(type);
        for (Type genericInterface : type.getGenericInterfaces())
        {
            if (genericInterface instanceof Class a)
            {
                findAllInterface(a, set);
            }
            else if (genericInterface instanceof ParameterizedType a)
            {
                findAllInterface((Class<?>) a.getRawType(), set);
            }
        }
    }

    private Type findReposositoryInterface(Type type)
    {
        if (type instanceof Class<?> a)
        {
            for (Type genericInterface : a.getGenericInterfaces())
            {
                if (genericInterface instanceof Class<?> b)
                {
                    Type result = findReposositoryInterface(b);
                    if (result != null)
                    {
                        return result;
                    }
                }
                else if (genericInterface instanceof ParameterizedType b)
                {
                    if (((Class) b.getRawType()) == Repository.class)
                    {
                        return b;
                    }
                    Type result = findReposositoryInterface(b.getRawType());
                    if (result != null)
                    {
                        return result;
                    }
                }
            }
        }
        else if (type instanceof ParameterizedType a)
        {
            if (a.getRawType() == Repository.class)
            {
                return a;
            }
            else
            {
                return findReposositoryInterface(a.getRawType());
            }
        }
        return null;
    }

    private void addFindOneMethod(ClassModel classModel, Class<?> repositoryClass, Class<?> descriptorClass)
    {
        MethodModel methodModel = new MethodModel(classModel);
        methodModel.setMethodName("findOne");
        methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        methodModel.setReturnType(repositoryClass);
        methodModel.setParamterTypes(Param.class);
        methodModel.setParamterNames("param");
        methodModel.setBody(STR.format("""
                                               if (JdbcTransactionManager.CONTEXT.get() == null)
                                                       {
                                                           throw new RuntimeException("请先开启事务");
                                                       }
                                               SqlSession sqlSession = sessionFactory.openSession();
                                               return sqlSession.getMapper({}.class).findOne(param);\r\n""", SmcHelper.getReferenceName(descriptorClass, classModel)));
        classModel.putMethodModel(methodModel);
    }

    private void addFindListMethod(ClassModel classModel, Class<?> repositoryClass, Class<?> descriptorClass)
    {
        MethodModel methodModel = new MethodModel(classModel);
        methodModel.setMethodName("findList");
        methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        methodModel.setReturnType(List.class);
        methodModel.setParamterTypes(Param.class);
        methodModel.setParamterNames("param");
        methodModel.setBody(STR.format("""
                                               if (JdbcTransactionManager.CONTEXT.get() == null)
                                                       {
                                                           throw new RuntimeException("请先开启事务");
                                                       }
                                               SqlSession sqlSession = sessionFactory.openSession();
                                               return sqlSession.getMapper({}.class).findList(param);\r\n""", SmcHelper.getReferenceName(descriptorClass, classModel)));
        classModel.putMethodModel(methodModel);
    }

    private void addFindListPageMethod(ClassModel classModel, Class<?> repositoryClass, Class<?> descriptorClass)
    {
        MethodModel methodModel = new MethodModel(classModel);
        methodModel.setMethodName("findList");
        methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        methodModel.setReturnType(List.class);
        methodModel.setParamterTypes(Param.class, Page.class);
        methodModel.setParamterNames("param", "page");
        methodModel.setBody(STR.format("""
                                               if (JdbcTransactionManager.CONTEXT.get() == null)
                                                       {
                                                           throw new RuntimeException("请先开启事务");
                                                       }
                                               SqlSession sqlSession = sessionFactory.openSession();
                                               return sqlSession.getMapper({}.class).findList(param, page);\r\n""", SmcHelper.getReferenceName(descriptorClass, classModel)));
        classModel.putMethodModel(methodModel);
    }

    private void addCountMethod(ClassModel classModel, Class<?> descriptorClass)
    {
        MethodModel methodModel = new MethodModel(classModel);
        methodModel.setMethodName("count");
        methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        methodModel.setReturnType(int.class);
        methodModel.setParamterTypes(Param.class);
        methodModel.setParamterNames("param");
        methodModel.setBody(STR.format("""
                                               if (JdbcTransactionManager.CONTEXT.get() == null)
                                                       {
                                                           throw new RuntimeException("请先开启事务");
                                                       }
                                               SqlSession sqlSession = sessionFactory.openSession();
                                               return sqlSession.getMapper({}.class).count(param);\r\n""", SmcHelper.getReferenceName(descriptorClass, classModel)));
        classModel.putMethodModel(methodModel);
    }

    private void addDeleteMethod(ClassModel classModel, Class<?> descriptorClass)
    {
        MethodModel methodModel = new MethodModel(classModel);
        methodModel.setMethodName("delete");
        methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        methodModel.setReturnType(int.class);
        methodModel.setParamterTypes(Param.class);
        methodModel.setParamterNames("param");
        methodModel.setBody(STR.format("""
                                               if (JdbcTransactionManager.CONTEXT.get() == null)
                                                       {
                                                           throw new RuntimeException("请先开启事务");
                                                       }
                                               SqlSession sqlSession = sessionFactory.openSession();
                                               return sqlSession.getMapper({}.class).delete(param);\r\n""", SmcHelper.getReferenceName(descriptorClass, classModel)));
        classModel.putMethodModel(methodModel);
    }

    private void addInsertMethod(ClassModel classModel, Class<?> repositoryClass, Class<?> descriptorClass)
    {
        MethodModel methodModel = new MethodModel(classModel);
        methodModel.setMethodName("insert");
        methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        methodModel.setReturnType(int.class);
        methodModel.setParamterTypes(repositoryClass);
        methodModel.setParamterNames("entity");
        methodModel.setBody(STR.format("""
                                               if (JdbcTransactionManager.CONTEXT.get() == null)
                                                       {
                                                           throw new RuntimeException("请先开启事务");
                                                       }
                                               SqlSession sqlSession = sessionFactory.openSession();
                                               return sqlSession.getMapper({}.class).insert(entity);\r\n""", SmcHelper.getReferenceName(descriptorClass, classModel)));
        classModel.putMethodModel(methodModel);
    }

    private void addSaveMethod(ClassModel classModel, Class<?> repositoryClass, Class<?> descriptorClass)
    {
        MethodModel methodModel = new MethodModel(classModel);
        methodModel.setMethodName("save");
        methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        methodModel.setReturnType(int.class);
        methodModel.setParamterTypes(repositoryClass);
        methodModel.setParamterNames("entity");
        methodModel.setBody(STR.format("""
                                               if (JdbcTransactionManager.CONTEXT.get() == null)
                                                       {
                                                           throw new RuntimeException("请先开启事务");
                                                       }
                                               SqlSession sqlSession = sessionFactory.openSession();
                                               return sqlSession.getMapper({}.class).save(entity);\r\n""", SmcHelper.getReferenceName(descriptorClass, classModel)));
        classModel.putMethodModel(methodModel);
    }

    private void addUpdateMethod(ClassModel classModel, Class<?> repositoryClass, Class<?> descriptorClass)
    {
        MethodModel methodModel = new MethodModel(classModel);
        methodModel.setMethodName("update");
        methodModel.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
        methodModel.setReturnType(int.class);
        methodModel.setParamterTypes(repositoryClass);
        methodModel.setParamterNames("entity");
        methodModel.setBody(STR.format("""
                                               if (JdbcTransactionManager.CONTEXT.get() == null)
                                                       {
                                                           throw new RuntimeException("请先开启事务");
                                                       }
                                               SqlSession sqlSession = sessionFactory.openSession();
                                               return sqlSession.getMapper({}.class).update(entity);\r\n""", SmcHelper.getReferenceName(descriptorClass, classModel)));
        classModel.putMethodModel(methodModel);
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
