package io.zhanghao.streaming

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * 基于Spark Streaming的日志分析
  *
  * @author 2019/03/27 zhanghao
  */
object LogApp {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("LogApp")
    val ssc = new StreamingContext(conf, Seconds(2))

    ssc.sparkContext.setLogLevel("warn")

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "spark",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("mytopic")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    val lines = stream.map(record => record.value)
    //总PV
    lines.count().print()

    //各 IP 的 PV ，按 PV 倒序。空格分隔的第一个字段就是 IP 。
    lines.map(_.split(" "))
      .map(x => (x(0), 1)).reduceByKey(_ + _).transform(
      rdd => rdd.sortBy(_._2)
    ).print()


    //搜索引擎的 PV 。
    val refer = lines.map(_.split("\"")(3))
    // 先输出搜索引擎和查询关键词，避免统计搜索关键词时重复计算
    // 输出(host, query_keys)
    val searchEnginInfo = refer.map(r => {

      val f = r.split('/')

      val searchEngines = Map(
        "www.google.cn" -> "q",
        "www.yahoo.com" -> "p",
        "cn.bing.com" -> "q",
        "www.baidu.com" -> "wd",
        "www.sogou.com" -> "query"
      )

      if (f.length > 2) {
        val host = f(2)

        if (searchEngines.contains(host)) {
          val query = r.split('?')(1)
          if (query.length > 0) {
            val arr_search_q = query.split('&').filter(_.indexOf(searchEngines(host) + "=") == 0)
            if (arr_search_q.length > 0)
              (host, arr_search_q(0).split('=')(1))
            else
              (host, "")
          } else {
            (host, "")
          }
        } else
          ("", "")
      } else
        ("", "")

    })

    // 输出搜索引擎PV
    searchEnginInfo.filter(_._1.length > 0).map(p => {
      (p._1, 1)
    }).reduceByKey(_ + _).print()


    //关键词的 PV
    searchEnginInfo
      .filter(_._2.length > 0)
      .map(p => {
        (p._2, 1)
      }).reduceByKey(_ + _).print()

    //终端类型的 PV 。
    lines.map(_.split("\"")(5))
      .map(agent => {
        val types = Seq("iPhone", "Android")
        var r = "Default"
        for (t <- types) {
          if (agent.indexOf(t) != -1)
            r = t
        }
        (r, 1)
      }).reduceByKey(_ + _).print()


    ssc.start()
    ssc.awaitTermination()

  }

}
