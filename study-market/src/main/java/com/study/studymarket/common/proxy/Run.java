package com.study.studymarket.common.proxy;

import java.lang.reflect.InvocationHandler;

public class Run {
    public static void main(String[] args) {
        //JDK动态代理
//        Cat cat = new ShowCatBody();//委托对象
//        InvocationHandler handler = new JDKShowProxy(cat);
//        Cat catModel = ((JDKShowProxy) handler).getProxyInstance();
//        catModel.foot("这是foot");

        ShowCatBody showCatBody = new ShowCatBody();
        CGLIBShowProxy proxy = new CGLIBShowProxy(showCatBody);
        ShowCatBody result = (ShowCatBody)proxy.getProxyInstance();
        result.foot("这是foot");
    }
}
