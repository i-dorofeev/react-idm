name := "core"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "Local Maven Repository" at "file:///C:/Users/Ilya/.m2home/repository"

libraryDependencies ++= Seq(
  "net.tirasa.connid" % "connector-framework" % "1.4.2.18",
  "net.tirasa.connid" % "connector-framework-internal" % "1.4.2.18",
  "org.scala-lang.modules" % "scala-java8-compat_2.11" % "0.8.0",
  "com.typesafe.akka" %% "akka-actor" % "2.4.17",
  "com.typesafe.akka" %% "akka-stream" % "2.4.17"
)

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.24"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0"
)

libraryDependencies += "com.h2database" % "h2" % "1.4.194" % "runtime"

libraryDependencies += "org.json4s" % "json4s-jackson_2.11" % "3.5.1"
libraryDependencies += "org.json4s" % "json4s-native_2.11" % "3.5.1"

