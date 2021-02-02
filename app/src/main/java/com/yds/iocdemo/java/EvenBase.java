package com.yds.iocdemo.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE) //写在其他自定义注解上
@Retention(RetentionPolicy.RUNTIME)
@interface EvenBase {
    //1,订阅关系
    String listenerSetter();
    //2，事件本身
    Class<?> listenerType();
    //3，事件处理程序
    String callbackMethod();
}

