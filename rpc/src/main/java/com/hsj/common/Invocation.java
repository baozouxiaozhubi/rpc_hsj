package com.hsj.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//服务调用者发送的请求的抽象，包含接口，方法，方法参数
//实际调用的时候，服务调用者通过代理进行序列化，发送被序列化的Invocation到服务提供者，服务提供者的代理接收Invocation，反序列化后执行方法
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Invocation implements Serializable {
    private String interfaceName;
    private String methodName;
    private Class[] parameterTypes; //参数类型的列表，用于方法重载(运行时多态)
    private Object[] parameters; //参数列表
}
