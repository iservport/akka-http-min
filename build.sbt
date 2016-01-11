packageArchetype.akka_application

name := "winnect-root"

version := "1.0"

scalaVersion := "2.10.6"

mainClass in Compile := Some("com.iservport.winnect.akka.Bootstrap")

libraryDependencies ++= Seq(
  "com.typesafe.akka"  %% "akka-kernel"  % "2.3.8",
  "com.typesafe.akka"  %% "akka-actor"   % "2.3.8",
  "com.typesafe.akka"  %% "akka-slf4j"   % "2.3.8",
  "com.typesafe.akka"  %% "akka-testkit" % "2.3.8",
  "com.typesafe.slick" %% "slick"        % "3.0.0",
  "com.typesafe.akka"  %% "akka-http-experimental" % "2.0.1",
  "mysql" % "mysql-connector-java" % "5.1.35",
  "org.ccil.cowan.tagsoup" % "tagsoup"               % "1.2",
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.2",
  "com.github.nscala-time" %% "nscala-time" % "2.0.0",
  "org.apache.spark"       %% "spark-core"  % "1.3.1",
  "com.datastax.spark"     %% "spark-cassandra-connector" % "1.3.0-M1"
)

// add resolvers

resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"

// append several options to the list of options passed to the Java compiler

javacOptions ++= Seq("-source", "1.7", "-target", "1.7", "-encoding", "UTF-8")

