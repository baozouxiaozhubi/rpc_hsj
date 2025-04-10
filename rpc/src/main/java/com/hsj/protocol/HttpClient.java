package com.hsj.protocol;

import com.hsj.common.Invocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//Tomcat是专门为HTTP设置的协议
public class HttpClient {

    //基于HTTP实现
    public String send(String hostname, Integer port, Invocation invocation)
    {
        String result=null;
        try {
            URL url = new URL("http", hostname, port, "/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true); //如果你需要通过这个 URL 连接向服务器发送数据，就把 doOutput 设为 true，默认是不发送的。

            //配置输出流
            OutputStream outputStream = httpURLConnection.getOutputStream();

            //ObjectOutputStream 是 Java 中的一个类，用于将 Java 对象序列化成字节流，可以写入文件、网络或其他输出流中，方便进行数据的保存或传输。
            try(ObjectOutputStream oos = new ObjectOutputStream(outputStream)){
                oos.writeObject(invocation);
                oos.flush();
            }
            catch (Exception e){throw e;}

            //阻塞式接受响应结果
            InputStream inputStream = httpURLConnection.getInputStream();
            result = IOUtils.toString(inputStream);
        }
        catch(Exception e){e.printStackTrace();}
        return result;
    }
}
