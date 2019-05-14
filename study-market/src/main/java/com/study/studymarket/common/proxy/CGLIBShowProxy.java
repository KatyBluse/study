package com.study.studymarket.common.proxy;



import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Date;

public class CGLIBShowProxy implements MethodInterceptor {

    //代理对象
    private Object obj;

    public CGLIBShowProxy(Object obj) {
        this.obj = obj;
    }

    public Object getProxyInstance() {
        Enhancer enhancer = new Enhancer();// [ɪn'hænsɚ] 增强器
        // 设置父类,因为Cglib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(this);
        Object result = enhancer.create();
        return result;
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("startDate:"+ new Date());
//        Object result = methodProxy.invokeSuper(o, objects);
        Object result = method.invoke(o, objects);
        System.out.println("endDate:"+ new Date());
        return result;
    }
}
