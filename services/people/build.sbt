name := "people-service"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.17",
  "com.typesafe.akka" %% "akka-remote" % "2.4.17"
)

resolvers += "Eventuate Released" at "https://dl.bintray.com/rbmhtechnology/maven"
libraryDependencies ++= Seq(
  "com.rbmhtechnology" %% "eventuate-core" % "0.8.1",
  "com.rbmhtechnology" %% "eventuate-log-leveldb" % "0.8.1"
)