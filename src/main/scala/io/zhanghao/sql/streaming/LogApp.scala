package io.zhanghao.sql.streaming

import com.typesafe.config.ConfigFactory
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
      .writeStream
      .outputMode("update")
      .foreach(writer(ProcessType.TOTAL_PV.toString)).start()

    import org.apache.spark.sql.functions._
    //各IP的PV
    val ipQuery = lines.select(split($"value", " ").getItem(0).as("ip"))
      .groupBy($"ip")
      .count()
      .writeStream
      .outputMode("update")
      .foreach(writer(ProcessType.IP_PV.toString)).start()

    ipQuery.awaitTermination()


    //搜索引擎的 PV 。
    //http://cn.bing.com/search?q=spark sql
    val refer = lines.select(split($"value", "\"").getItem(3).as("refer")).as[String]
      .where(length($"refer") > 1)

    val searchEngineQuery = refer.select(split($"refer", "\\/").getItem(2).as("search_engine"))
      .groupBy($"search_engine")
      .count()
      .writeStream
      .outputMode("update")
      .foreach(writer(ProcessType.SEARCH_ENGINE_PV.toString)).start()

    searchEngineQuery.awaitTermination()


    //关键词PV
    //http://cn.bing.com/search?q=spark sql
    val keyWordQuery = refer.select(split($"refer", "=").getItem(1).as("keyWord"))
      .groupBy("keyWord")
      .count()
      .writeStream
      .outputMode("update")
      .foreach(writer(ProcessType.KEYWORD_PV.toString)).start()

    keyWordQuery.awaitTermination()


    //终端PV
    val query = lines
      .select(split($"value", "\"").getItem(5).as("agent"))
      .where(col("agent").isNotNull)
      .select(
        when(instr(col("agent"), "Android") > 0, "Android")
          .when(instr(col("agent"), "iPhone") > 0, "iPhone")
          .otherwise("Default").as("agent")
      ).groupBy("agent").count()
      .writeStream
      .outputMode("update")
      .foreach(writer(ProcessType.AGENT_PV.toString)).start()


    query.awaitTermination()


  }


  def writer(processType: String): JDBCSink = {
    val config = ConfigFactory.load()
    new JDBCSink(
      url = config.getString("url"),
      username = config.getString("username"),
      password = config.getString("password"), processType)
  }


}
