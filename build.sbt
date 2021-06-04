name := "heimdall-api-v2"

version := "0.1"

scalaVersion := "2.12.6"


mainClass in Compile := Some("Application")


val macwire = "2.3.0"
val json4s = "3.6.0"
val sttp = "1.1.13"

credentials += Credentials(realm = "Artifactory Realm", host = "artifactory.joveo.com", userName = "joveodev", passwd = "AyR^aW4D6p@s")

libraryDependencies ++= Seq(

  "de.heikoseeberger" %% "akka-http-json4s" % "1.21.0",
  "org.json4s" %% "json4s-core" % json4s,
  "org.json4s" %% "json4s-native" % json4s,
  "org.json4s" %% "json4s-ext" % json4s,

  "com.softwaremill.macwire" %% "macros" % macwire,
  "com.softwaremill.macwire" %% "util" % macwire,

  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "org.mongodb.scala" %% "mongo-scala-driver" % "2.4.0",
  "joda-time" % "joda-time" % "2.10",
  "org.joda" % "joda-money" % "1.0.1",

  "com.lightbend.akka" %% "akka-stream-alpakka-sqs" % "0.20",

  "com.softwaremill.sttp" %% "core" % sttp,
  "com.softwaremill.sttp" %% "async-http-client-backend-future" % sttp,

  "com.github.etaty" %% "rediscala" % "1.8.0",
  "com.beachape" %% "enumeratum" % "1.5.13",

  "io.kamon" %% "kamon-core" % "0.6.6",
  "io.kamon" %% "kamon-akka-2.4" % "0.6.6",
  "io.kamon" %% "kamon-akka-http" % "0.6.6",
  "io.kamon" %% "kamon-datadog" % "0.6.6",
  "io.kamon" %% "kamon-autoweave" % "0.6.5",
  "org.aspectj" % "aspectjweaver" % "1.9.2",

  "com.amazonaws" % "aws-java-sdk-secretsmanager" % "1.11.616",
  "org.apache.commons" % "commons-text" % "1.3",
  "de.siegmar" % "fastcsv" % "1.0.3",
  // "net.liftweb" %% "lift-json" % "2.6.2",
  // "org.apache.commons" % "commons-lang3" % "3.10",
  "com.github.seratch" %% "awscala" % "0.5.+",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.529",
  "org.scalatest" %% "scalatest" % "3.0.0",
  "org.mockito" % "mockito-all" % "1.9.5",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.3",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % "2.11.3",
  "io.spray" %% "spray-json" % "1.3.2",
  "com.joveo" %% "apiutils" % "1.0.1-SNAPSHOT",
  "software.amazon.awssdk" % "cognitoidentityprovider" % "2.16.40"
)
