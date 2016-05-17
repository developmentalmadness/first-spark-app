import sbt._
import sbt.Keys._

object BuildSettings {

  val Name = "first-spark-app"

  // WARNING: If you change the version, change it in ALL
  // places where it might appear, including the notebook (.snb)
  // files, the `conf/spark-notebook/application.conf`, and possibly
  // other places, too!!
  val Version = "4.0.2"

  // Works just as well with both Scala 2.10.X and 2.11.X.
  // To switch versions at the sbt prompt use:
  //   ++ 2.10.6
  // or
  //   ++ 2.11.7
  val ScalaVersion = "2.11.7"
  val ScalaVersions = Seq("2.11.7", "2.10.6")

  lazy val buildSettings = Defaults.coreDefaultSettings ++ Seq (
    name          := Name,
    version       := Version,
    scalaVersion  := ScalaVersion,
    organization  := "com.lightbend",
    description   := "Spark Workshop",
    scalacOptions := Seq("-deprecation", "-feature", "-unchecked", "-encoding", "utf8", "-Xlint") //, "-Xprint:parser")
  )
}


object Resolvers {
  val lightbend = "Typesafe Repository" at "http://repo.lightbend.com/lightbend/releases/"
  val sonatype  = "Sonatype Release" at "https://oss.sonatype.org/content/repositories/releases"
  val mvnrepo   = "MVN Repo" at "http://mvnrepository.com/artifact"

  val allResolvers = Seq(lightbend, sonatype, mvnrepo)

}

object Dependency {
  object Version {
    // WARNING: If you change Spark version, find all occurrences in READMEs,
    // the course Setup.html and Index.html deck files, and notebooks.
    val Spark        = "1.6.0"
    val Hadoop       = "2.6.0"
    val JavaX        = "3.0.1"

    val ScalaTest    = "2.2.4"
    val ScalaCheck   = "1.12.2"
  }

  val sparkCore      = "org.apache.spark"  %% "spark-core"      % Version.Spark  withSources()
  val sparkStreaming = "org.apache.spark"  %% "spark-streaming" % Version.Spark  withSources()
  val sparkSQL       = "org.apache.spark"  %% "spark-sql"       % Version.Spark  withSources()
  val sparkRepl      = "org.apache.spark"  %% "spark-repl"      % Version.Spark  withSources()
  val sparkMLlib     = "org.apache.spark"  %% "spark-mllib"     % Version.Spark  withSources()
  val sparkHive      = "org.apache.spark"  %% "spark-hive"      % Version.Spark  withSources()

  // These two dependencies exist only to force Windows-compatible versions of Hadoop
  // (first one) and consistent versions of javax.servlet (second one).
  val hadoopClient   = "org.apache.hadoop"  % "hadoop-client"     % Version.Hadoop
  val javaX          = "javax.servlet"      % "javax.servlet-api" % Version.JavaX

  // For testing.
  val scalaTest      = "org.scalatest"     %% "scalatest"       % Version.ScalaTest  % "test"
  val scalaCheck     = "org.scalacheck"    %% "scalacheck"      % Version.ScalaCheck % "test"

  val playJson       = "com.typesafe.play" %% "play-json"       % "2.3.9"
}

object Dependencies {
  import Dependency._

  val sparkWorkshop =
    Seq(hadoopClient, javaX,
      sparkCore, sparkStreaming, sparkSQL, sparkHive, sparkMLlib, // sparkRepl,
      scalaTest, scalaCheck, playJson)
}

object SparkWorkshopBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  lazy val firstApp = Project(
    id = "first-spark-app",
    base = file("."),
    settings = buildSettings ++ Seq(
      cleanFiles += file("output"),
      cleanKeepFiles += file("output") / "README.markdown",
      maxErrors := 5,
      // Suppress warnings about Scala patch differences in dependencies.
      // This is slightly risky, so consider not doing this for production
      // software, see what the warnings are using the sbt 'evicted' command,
      // then "ask your doctor if this setting is right for you..."
      ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
      triggeredMessage := Watched.clearWhenTriggered,
      // runScriptSetting,
      resolvers := allResolvers,
      libraryDependencies ++= Dependencies.sparkWorkshop,
      unmanagedResourceDirectories in Compile += baseDirectory.value / "conf",
      mainClass := Some("run"),
      // Sometimes, it works better to run the examples and tests in separate JVMs:
      // fork := true,
      //This is important for some programs to read input from stdin
      connectInput in run := true,
      // Must run Spark tests sequentially because they compete for port 4040!
      // TODO. There is now a Spark property to disable the web console. If we
      // use it, then we can remove the following setting:
      parallelExecution in Test := false,
      testOptions in Test += Tests.Argument("-oF")))
}
