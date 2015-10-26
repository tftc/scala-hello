import sbt.Keys._
import sbtunidoc.Plugin.UnidocKeys._

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
  val spring = "3.2.13.RELEASE"
  val mybatisSpring = "1.2.3"
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

val baseSettings = Seq(
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % versions.logback % "test",
    "org.mockito" % "mockito-core" % "1.9.5" % "test",
    "org.scalatest" %% "scalatest" % "2.2.3" % "test",
    "org.specs2" %% "specs2" % "2.3.12" % "test"
  ),
  resolvers ++= Seq(
    Resolver.mavenLocal,
    "qicheng maven" at "http://123.57.5.3:9092/nexus/content/groups/public",
    "tiancai maven" at "http://123.57.227.107:8086/nexus/content/groups/tftiancai-nexus-group",
    Resolver.sonatypeRepo("releases")
  ),
  compilerOptions
)

lazy val cubeBuildSettings = baseSettings ++ buildSettings  ++ Seq(
  organization := "com.itiancai"
)


lazy val commonSettings = baseSettings ++ buildSettings ++ unidocSettings

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


lazy val thriftExample = (project in file("thrift-example")).enablePlugins(ScroogeSBT)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.twitter" %% "scrooge-core" % "4.0.0",
      "com.twitter" %% "finagle-thrift" % "6.28.0",
      "org.mybatis" % "mybatis" % versions.mybatis,
      "org.mybatis" % "mybatis-spring" % versions.mybatisSpring,
      "org.springframework" % "spring-context" % versions.spring,
      "redis.clients" % "jedis" % "2.7.2"
    )
  ).settings(
    scroogeThriftOutputFolder in Compile <<= baseDirectory {
      base => base / "src/main/java2"
    }
  )



//lazy val generateCode = project.in(file("generateCode")).settings(commonSettings: _*)
//  .enablePlugins(ScroogeSBT).settings(
//    scroogeThriftOutputFolder in Compile <<= baseDirectory {
//      base => base / "src/main/java"
//    },
//    scroogeLanguage in Compile := "java"
//  )