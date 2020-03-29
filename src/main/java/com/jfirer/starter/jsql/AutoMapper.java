package com.jfirer.starter.jsql;

import com.jfirer.jfire.core.beanfactory.SelectBeanFactory;
import com.jfirer.jsql.mapper.Mapper;

import javax.annotation.Resource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SelectBeanFactory(beanFactoryType = MapperFactory.class)
@Resource
@Mapper
public @interface AutoMapper
{
}
