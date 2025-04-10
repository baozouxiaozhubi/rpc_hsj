启动Consumer中的Consumer和Producer中的Producer和Producer2，试用远程服务调用
可以在Producer的配置文件rpcSettings.yml中配置如下信息
connect_type:
  netty/tomcat
hostname:
  localhost
port:
  8085
LB:
  Random/Round Robin/Consistent Hash/Least Connection
