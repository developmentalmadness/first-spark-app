# first-spark-app
Sample project demonstrating the setup for an Apache Spark project which doesn't require a local Spark installation and includes a REPL

#Prerequisites:

* [java 7 or 8 (prefer 8)](http://www.java.com/en/download/help/index_installing.xml)
* [sbt](http://www.scala-sbt.org/)
  * [OS X](http://www.scala-sbt.org/release/docs/Installing-sbt-on-Mac.html)
  * [Windows](http://www.scala-sbt.org/release/docs/Installing-sbt-on-Windows.html)
  * [Linux](http://www.scala-sbt.org/release/docs/Installing-sbt-on-Linux.html)
  * [Manual Install](http://www.scala-sbt.org/release/docs/Manual-Installation.html)
* Windows Users need `WinUtils.exe` installed:
  * [Download WinUtils.exe](http://public-repo-1.hortonworks.com/hdp-win-alpha/winutils.exe)
  * You don't need Hadoop installed, but wherever you put `winutils.exe` it needs to be found at `%HADOOP_HOME%\bin`
  * Define `JAVA_OPTS` to be `-Dhadoop.home.dir=C:\hadoop` (assuming you put `winutils.exe` at `C:\hadoop\bin`). Append this setting if you already have `JAVA_OPTS` defined.
  
  
