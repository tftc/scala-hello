import sbt.Keys._


lazy val buildSettings = Seq(
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.7",
  crossScalaVersions := Seq("2.10.5", "2.11.7")
)

lazy val versions = new {
  val finatra = "2.0.1"
  val finagle = "6.29.0"
  val logback = "1.0.13"
  val mybatis = "3.3.0"
  val spring = "3.2.15.RELEASE"
  val mybatisSpring = "1.2.3"
  val slf4j = "1.7.12"
}

lazy val compilerOptions = scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen"
) ++ (
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 11)) => Seq("-Ywarn-unused-import")
    case _ => Seq.empty
  }
)



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
  //licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  //homepage := Some(url("https://github.com/twitter/finatra")),
  autoAPIMappings := true,
  //apiURL := Some(url("https://twitter.github.io/finatra/docs/")),
//  pomExtra := (
//    <scm>
//      <url>git://github.com/twitter/finatra.git</url>
//      <connection>scm:git://github.com/twitter/finatra.git</connection>
//    </scm>
//      <developers>
//        <developer>
//          <id>twitter</id>
//          <name>Twitter Inc.</name>
//          <url>https://www.twitter.com/</url>
//        </developer>
//      </developers>
//    ),
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


val baseSettings = Seq(
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % versions.logback % "test",
    "org.mockito" % "mockito-core" % "1.9.5" % "test",
    "org.scalatest" %% "scalatest" % "2.2.3" % "test",
    "org.specs2" %% "specs2" % "2.3.12" % "test"
  ),
  resolvers ++= Seq(
    Resolver.mavenLocal,
    "qicheng" at "http://123.57.5.3:9092/nexus/content/groups/public",
    "tiancai" at "http://123.57.227.107:8086/nexus/content/groups/tftiancai-nexus-group",
    "oschina2" at "http://maven.oschina.net/content/groups/public",
    Resolver.sonatypeRepo("releases")
  ),
  compilerOptions,
  javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
)




lazy val commonSettings = baseSettings ++ publishSettings ++ buildSettings ++ unidocSettings

lazy val galaxySettings = commonSettings ++ Seq(
  organization := "com.itiancai.galaxy"
)

lazy val root = (project in file(".")).settings(commonSettings: _*)
  .aggregate(
    thriftExample,
    finatraExample
  )


lazy val finatraExample = (project in file("finatra-example"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.twitter.finatra" %% "finatra-http" % versions.finatra,
      "com.twitter.finatra" %% "finatra-slf4j" % versions.finatra,
      "ch.qos.logback" % "logback-classic" % versions.logback
    )
  )


lazy val thriftExample = (project in file("thrift-example"))
  .settings(name :="galaxy")
  .settings(galaxySettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.twitter" %% "scrooge-core" % "4.2.0",
      "com.twitter" %% "finagle-thrift" % versions.finagle,
      "com.twitter.inject" %% "inject-core" % "2.0.1",
      "org.mybatis" % "mybatis" % versions.mybatis,
      "org.mybatis" % "mybatis-spring" % versions.mybatisSpring,
      "org.springframework" % "spring-context" % versions.spring,
      "redis.clients" % "jedis" % "2.7.2",
      "joda-time" % "joda-time" % "2.8.2",
      "ch.qos.logback" % "logback-classic" % versions.logback,
      "org.slf4j" % "jcl-over-slf4j" % versions.slf4j,
      "org.slf4j" % "jul-to-slf4j" % versions.slf4j
    ),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
  )
  .enablePlugins(ScroogeSBT)
  //hot reload
  .settings(Revolver.settings:_*)
  .settings(
    mainClass in Revolver.reStart := Some("com.itiancai.passport.thrift.TestServer"),
    javaOptions in Revolver.reStart +=  "-Dgalaxias.env=dev",
    Revolver.enableDebugging(port = 5005, suspend = true),
    logLevel := sbt.Level.Info
  )
  .settings(
    mainClass in assembly := Some("com.itiancai.passport.thrift.TestServer"),
    assemblyMergeStrategy in assembly := {
       case x if Assembly.isConfigFile(x) =>
          MergeStrategy.concat
       case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
          MergeStrategy.rename
       case PathList("META-INF", xs @ _*) =>
          (xs map {_.toLowerCase}) match {
              case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
                  MergeStrategy.discard
              case ps @ (x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
                  MergeStrategy.discard
              case "plexus" :: xs =>
                  MergeStrategy.discard
              case "spring.tooling" :: xs =>
                  MergeStrategy.discard
              case "services" :: xs =>
                  MergeStrategy.filterDistinctLines
              case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
                  MergeStrategy.filterDistinctLines
              case _ => MergeStrategy.deduplicate
          }
        case "asm-license.txt" | "overview.html" =>
          MergeStrategy.discard
        case _ => MergeStrategy.deduplicate
    }
  )



//lazy val generateCode = project.in(file("generateCode")).settings(commonSettings: _*)
//  .enablePlugins(ScroogeSBT).settings(
//    scroogeThriftOutputFolder in Compile <<= baseDirectory {
//      base => base / "src/main/java"
//    },
//    scroogeLanguage in Compile := "java"
//  )