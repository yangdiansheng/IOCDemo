package com.yds.iocdemo.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 用来代理 View.OnclickListener() 对象，并执行对象身上的onclick方法
 */
public class ListenerInvocationHandler implements InvocationHandler {

    //需要onClick中执行Activity.click()
    private Object activity;
    private Method activityMethod;

    public ListenerInvocationHandler(Object activity, Method activityMethod) {
        this.activity = activity;
        this.activityMethod = activityMethod;
    }

    /**
     * 表示onclick的执行
     * 框架中不直接执行onclick
     * 关联Activity中的onclick
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在这里调用被注解了的Activity click
        return activityMethod.invoke(activity,args);
    }
}
