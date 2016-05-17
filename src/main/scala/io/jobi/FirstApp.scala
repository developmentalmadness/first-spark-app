package io.jobi

import org.apache.spark.{SparkConf, SparkContext}

object FirstApp {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("FirstSparkApp")
    val sc = new SparkContext("local[*]", "First App", conf)

    try {
      val input = sc.textFile("data/all-shakespeare.txt").map(_.toLowerCase())

      val wc = input
        .flatMap(text => text.split("""\W+"""))
        .map(word => (word,1))
        .reduceByKey(_ + _)

      val outpath = s"output/shakespeare-wc/${System.currentTimeMillis()}"
      println(s"Writing output to: $outpath")
      wc.saveAsTextFile(outpath)
    }
    finally {
      sc.stop()
    }
  }
}
