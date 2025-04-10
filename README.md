# 🚀 HSJ-RPC: A Lightweight Java RPC Framework

> 作者：黄舒俊 | 厦门大学  
> 本项目是用于学习的的远程服务框架实现。

---

## 📌 项目介绍

**HSJ-RPC** 是一个基于 Java 实现的轻量级 RPC 框架，支持 Netty / Tomcat 通信协议切换，支持多种负载均衡策略、服务注册与发现机制，且配置灵活、模块解耦，适合学习与工程实践使用。

---

## ✨ 项目功能

- ✅ 支持 Netty / Tomcat 双通信协议
- ✅ 多种负载均衡策略（随机、轮询、一致性哈希、最小连接数）
- ✅ Redis 实现的服务注册与发现中心，心跳保活机制
- ✅ 动态代理 + Hessian 序列化
- ✅ 支持 `.yml` 零侵入式配置
- ✅ 支持本地与远程服务调用自动区分


---

## ⚙️ 配置文件说明（`rpcSettings.yml`）

```yaml
# 连接协议类型：支持 netty / tomcat
connect_type: netty

# 服务提供者主机地址
hostname: localhost

# 服务端口
port: 8085

# 负载均衡策略（任选其一）
# 可选：Random / Round Robin / Consistent Hash / Least Connection
LB: Random

## ✨ 启动步骤
启动 Redis 注册中心（默认端口 6379）

启动 Producer 和 Producer2

启动 Consumer

调用远程方法，观察控制台打印的服务调用过程

