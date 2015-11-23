##scala-hello使用
该项目主要为scala相关的一些实验性项目


###sbt相关

启用私服
新增~/.sbt/repositories文件，在文件中增加如下
resolvers在项目中


###fingale-example

####thrift

IDL描述:

http://diwakergupta.github.io/thrift-missing-guide

如何生成文件

1. 默认读取`src/thrift`目录中的文件
2. 生成文件位置为当前`target/scala-{版本号}/src_manager`
3. 生成使用`${项目名}/compile:scroogeGen`, 例如：`thriftExample/compile:scroogeGen`，不会引起编译冲突




###如何热记载并启动调试
* 使用resolver插件
    在sbt中键入~reStart即可监测当前所有文件改动，自行重启.
    在`build.sbt`的setting中增加 `Revolver.enableDebugging(port = 5005, suspend = true),`
* 使用jrebel热加载




###环境配置（待完成）
程序启动时候需要加javaOptions
1. 系统自动读取-denv=dev的配置项目，如果没有默认为dev环境.
2. config目录下将有dev,test,pre-test,production
3. 注意将envirment的process进行扩展，默认读取config目录下的所有相关配置.
4. 读取的配置会自动与spring集成


###日志（待完成）
1. 将使用ls4j的配置
2. 扩展scala的log配置


###服务器扩展（待完成）
1. 留出spring扩展点以供扩展
2. 默认服务器的相关配置，自动使用server.properties进行配置


###服务器开发
尽量不要在java中使用scala代码，有些使用起来比较反人类


 
 