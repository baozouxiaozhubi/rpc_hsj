package com.hsj.protocol;

import com.hsj.utils.HessianUtils;
import com.hsj.common.Invocation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NettyClient {
    public String send(String hostname, Integer port, Invocation invocation) {
        String result = "";
        try (Socket socket = new Socket(hostname, port)) {
            // 1. Hessian 序列化 Invocation
            byte[] data = HessianUtils.serialize(invocation);

            // 2. 写入长度 + 实际数据
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeInt(data.length); // 写入4字节长度头
            dos.write(data);           // 写入实际序列化内容
            dos.flush();

            // 3. 接收响应（带粘包头）
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int len = dis.readInt();  // 读取4字节的响应长度
            byte[] respBytes = new byte[len];
            dis.readFully(respBytes); // 读取完整响应体

            result = (String) HessianUtils.deserialize(respBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
