##scala-hello使用
该项目主要为scala相关的一些实验性项目


###sbt相关

###启用私服
新增[~/.sbt/repositories](http://wiki.itiancai.com/download/attachments/3607277/repositories?api=v2)文件

###发布
在配置中增加以下配置文件用于发布
  
```
lazy val publishSettings = Seq(  
  publishMavenStyle := true,    
  publishArtifact := true,    
  publishArtifact in Test := false,
  publishArtifact in (Compile, packageDoc) := true,
  publishArtifact in (Test, packageDoc) := true,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "http://123.57.227.107:8086/nexus/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  autoAPIMappings := true,
  pomPostProcess := { (node: scala.xml.Node) =>
    val rule = new scala.xml.transform.RewriteRule {
      override def transform(n: scala.xml.Node): scala.xml.NodeSeq =
        n.nameToString(new StringBuilder()).toString() match {
          case "dependency" if (n \ "groupId").text.trim == "org.scoverage" => Nil
          case _ => n
        }
    }
    new scala.xml.transform.RuleTransformer(rule).transform(node).head
  }
)
```

发布时候需要nexus的deploy权限，配置如下：

```
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
```
.credentials文件如下：

```
realm=Sonatype Nexus Repository Manager
host=123.57.227.107
user=deployment
password=xxxxxx
```

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
* 使用[JRebel](http://wiki.itiancai.com/download/attachments/3607277/jrebel_6.2.5-agent-crack.zip?api=v2)热加载
   启动参数增加 `-agentpath:/jrebel的path路径/lib/libjrebel64.dylib -Drebel.disable_update=true`



###环境配置
程序启动时候需要加javaOptions  
1. 系统自动读取 `-Dgalaxias.env={环境}` 的配置项目，默认为 `dev` ，加载 `config/dev/**/*.properties` 文件  
2. 该配置文件自动与spring集成，支持所有spring满足的方式, 如何使用参考Spring文档



###thrift使用
直接继承ThriftServer来使用，具体参考ExampleServer写法