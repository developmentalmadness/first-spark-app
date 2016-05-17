shellPrompt := { state => "sbt> " }

initialCommands += """
  println("setting up SparkContext....")
  import org.apache.spark.{SparkContext, SparkConf}
  import org.apache.spark.SparkContext._
  import org.apache.spark.sql.SQLContext
  // Hack: remove stale Hive metastore locks from prior sessions, if any.
  com.lightbend.training.util.FileUtil.rmrf(
    new java.io.File("metastore_db/db.lck"),
    new java.io.File("metastore_db/dbex.lck"))

  val sparkConf = new SparkConf()
  sparkConf.setMaster("local[*]")
  sparkConf.setAppName("Spark Console")
  // Silence annoying warning from the Metrics system:
  sparkConf.set("spark.app.id", "Spark Console")
  val sc = new SparkContext(sparkConf)
  val sqlContext = new SQLContext(sc)
  import sqlContext.implicits._
  sqlContext.setConf("spark.sql.shuffle.partitions", "4")
  """

cleanupCommands += """
  println("Closing the SparkContext:")
  sc.stop()
  """

addCommandAlias("first-app",               "runMain io.jobi.FirstApp")
