package com.hsj.register;

import java.util.HashMap;
import java.util.Map;

//本地注册表：Mao只能在本地使用,服务调用者和服务提供者要在一个JVM下
public class LocalRegister {
    private static Map<String,Class> map = new HashMap<>();

    public static void regist(String className, Class clazz) {
        map.put(className, clazz);
    }

    public static Class get(String interfaceName) {
        return map.get(interfaceName);
    }
}
