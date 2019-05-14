package com.study.studymarket.common.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

/**
 * 代理类
 *
 * 每一个动态代理类都必须要实现InvocationHandler这个接口，并且每个代理类的实例都关联到了一个handler，
 * 当我们通过代理对象调用一个方法的时候，这个方法的调用就会被转发为由InvocationHandler这个接口的
 * invoke 方法来进行调用。
 *
 * */
public class JDKShowProxy implements InvocationHandler {


    //委托类
    private Object obj;

    public JDKShowProxy(Object obj) {
        //这里我们通过构造器方式将委托类拼进去
        this.obj = obj;
    }

    /*
     * 通过Proxy的newProxyInstance方法来创建我们的代理对象，我们来看看其三个参数
     * 第一个参数 ClassLoader loader 代理对象, 说白了用谁来代理，就用谁到类加载器
     * 第二个参数 Class<?>[] interfaces我们这里为代理对象提供的接口是真实对象所实行的接口，
     *                                表示我要代理的是该真实对象，这样我就能调用这组接口中的方法了
     * 第三个参数 InvocationHandler h， 我们这里将这个代理对象关联到了上方的 InvocationHandler 这个对象上
     */
    public Cat getProxyInstance() {
        return (Cat) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                this);
    }

    /**
     * @param proxy:　　指代我们所代理的那个真实对象
     * @param method:　　指代的是我们所要调用真实对象的某个方法的Method对象
     * @param args:　　指代的是调用真实对象某个方法时接受的参数
     * */
    @Override

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("start time:" + new Date());

        //    代理使用委托类到方法时，通过invoke执行
        Object result = method.invoke(obj, args);

        //　　在代理真实对象后我们也可以添加一些自己的操作
        System.out.println("end time:" + new Date());
        return result;
    }
}
