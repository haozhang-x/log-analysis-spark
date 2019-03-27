package io.zhanghao.sql.streaming

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * 基于 Spark Structured Streaming 的日志分析
  *
  * @author 2019/03/27 zhanghao
  */
object LogApp {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("LogApp")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    import spark.implicits._
    //从Kafka中读取数据进来
    val lines = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "mytopic")
      .load().selectExpr("CAST(value AS STRING)").as[String]

    //总PV
    lines.groupBy().count()

    import org.apache.spark.sql.functions._
    //各IP的PV
    lines.select(split($"value", " ").getItem(0).as("ip"))
      .groupBy($"ip")
      .count()
      .orderBy($"count")


    //搜索引擎的 PV 。
    //http://cn.bing.com/search?q=spark sql
    val refer = lines.select(split($"value", "\"").getItem(3).as("refer")).as[String]
      .where(length($"refer") > 1)

    refer.select(split($"refer", "\\/").getItem(2).as("refer"))
      .groupBy($"refer")
      .count()


    //关键词PV
    //http://cn.bing.com/search?q=spark sql
    refer.select(split($"refer", "=").getItem(1).as("keyWord"))
      .groupBy("keyWord")
      .count()


    //终端PV
    val query = lines
      .select(split($"value", "\"").getItem(5).as("agent"))
      .where($"agent" =!= null)
      .select(expr("*"),
        when(instr(col("agent"), "Android") === 1, "Android")
          .when(instr(col("agent"), "iPhone") === 1, "iPhone")
          .otherwise("Default")
      )
      .writeStream
      .format("console")
      .outputMode("update")
      .start()

    query.awaitTermination()


  }

}
