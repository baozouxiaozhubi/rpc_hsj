package com.hsj;



public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return(name + "说Hello World");
    }
}
