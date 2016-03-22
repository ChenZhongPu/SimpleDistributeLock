# 简单的分布式锁实现

- [English Version](README_en.md)
- [中文版本](README.md)

来自阿里巴巴云计算课程的实践项目。由于项目要求，基于JDK 6。

## 目录结构

- `src`文件夹是工程源码，其中`com.team6.sjtu.test`是测试代码所在包
- `docs`是生成的JavaDoc
- `*.jar` 是工程依赖的第三方包，分别是用于JSON解析和单元测试
- `sources.txt`是所有源码清单
- `serverConfig.xml`是服务器配置文件
- `build.sh`是编译脚步，`startServer.sh`是启动服务器脚本，`stopServer.sh`停止服务器脚本，`restartServer.sh`是重启服务器脚本，`runCTest.sh`是受控条件下单元测试脚本

其中，`sources.txt`是为了便于编译生成的：

```shell
$ find . "*.java" > sources.txt
```

## 编译和运行

编译代码：
```shell
$ sh build.sh
```
编译后会生成`classout`文件夹。

```shell
# 启动服务器
$ sh startServer.sh
# 停止服务器
$ sh stopServer.sh
# 重启服务器
$ sh restartServer.sh
```
三个服务器会分别监听4444、4445、4446端口。若需修改或增加服务器配置，请确保`serverConfig.xml`文件和`startServer.sh`、`stopServer.sh`、`restartServer.sh`脚本里面的参数一致。

## 测试
受控的单元测试：在给定客户端请求次序下，测试结果是否和预期一致。
```shell
# 受控的单元测试
$ sh runCTest.sh
```
