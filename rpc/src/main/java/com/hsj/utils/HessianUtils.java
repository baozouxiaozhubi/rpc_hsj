package com.hsj.utils;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianUtils {

    // 将对象序列化为字节数组
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
        hessianOutput.writeObject(obj);  // 序列化对象
        hessianOutput.flush();  // 刷新输出流
        return byteArrayOutputStream.toByteArray();  // 返回序列化后的字节数组
    }

    // 将字节数组反序列化为对象
    public static Object deserialize(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
        return hessianInput.readObject();  // 反序列化并返回对象
    }
}
