name := "spark-stream"

version := "0.1"

scalaVersion := "2.11.8"

val root = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "2.4.8",
      "org.apache.spark" %% "spark-sql" % "2.4.8",
      "org.apache.spark" %% "spark-streaming" % "2.4.8",
      "org.apache.spark" %% "spark-mllib" % "2.4.8",

      "com.cloudera.sparkts" % "sparkts" % "0.4.1",


    )
  )
