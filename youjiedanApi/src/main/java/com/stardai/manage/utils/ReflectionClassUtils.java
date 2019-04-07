package com.stardai.manage.utils;

import com.stardai.manage.classloader.HotLoadClassLoader;

import java.lang.reflect.Method;

/**
 * 反射帮助类
 *
 * @author yax
 * @create 2019-01-16 16:13
 **/
public class ReflectionClassUtils {
    public static Object invokeMethod(Object target,String className,String methodName,Object... args){
        try {
            //ClassLoader hotLoadClassLoader = HotLoadClassLoader.getSingleClassLoader();
            ClassLoader classLoader=ReflectionClassUtils.class.getClassLoader();
            Class clz = classLoader.loadClass(className);
            Class[] argsClass=new Class[args.length];
            for(int i=0;i<args.length;i++){
                argsClass[i]=args[i].getClass();
            }
            Method method = clz.getMethod(methodName,argsClass);
            return method.invoke(target, args);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
