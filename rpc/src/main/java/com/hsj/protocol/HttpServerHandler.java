package com.hsj.protocol;

import com.hsj.common.Invocation;
import com.hsj.register.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//每一个不同种类的请求(服务调用，健康检查，配置同步......)都对应一个Handler类
//用于处理【服务调用】的请求
public class HttpServerHandler {
    public void handle(HttpServletRequest req, HttpServletResponse resp)
    {
        //关键点：获取Consumer需要调用的接口，方法，方法参数
        try
        {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();
            String interfaceName = invocation.getInterfaceName();

            //从本地注册表中获取接口的实现类
            Class classImpl = LocalRegister.get(interfaceName);

            //从实现类中获取需要执行的方法
            Method method = classImpl.getMethod(invocation.getMethodName(), invocation.getParameterTypes());

            //通过反射执行方法 在指定的对象(第一个参数是实现类的对象)上调用这个方法，并传入参数。
            String Result = (String) method.invoke(classImpl.newInstance(),invocation.getParameters());

            //把结果写入HttpSevletResponse-用IOUtils
            IOUtils.write(Result, resp.getOutputStream());
            System.out.println(Result);
        }
        catch(Exception e){e.printStackTrace();}
    }
}
