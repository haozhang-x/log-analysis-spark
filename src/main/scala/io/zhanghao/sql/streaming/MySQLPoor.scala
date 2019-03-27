package io.zhanghao.sql.streaming

import java.sql.{Connection, DriverManager}
import java.{util => ju}

import scala.annotation.tailrec

/**
  * MySQL 连接池
  *
  * @author 2019/03/27 zhanghao
  */
class MySQLPoor(url: String, user: String, pwd: String) extends Serializable {
  //连接池连接总数
  private val max = 3

  //每次产生连接数
  private val connectionNum = 1

  //当前连接池已产生的连接数
  private var conNum = 0

  private val pool = new ju.LinkedList[Connection]() //连接池

  //获取连接
  def getJdbcConn: Connection = {
    //同步代码块，AnyRef为所有引用类型的基类，AnyVal为所有值类型的基类
    AnyRef.synchronized({
      if (pool.isEmpty) {
        //加载驱动
        preGetConn()
        for (i <- 1 to connectionNum) {
          val conn = DriverManager.getConnection(url, user, pwd)
          pool.push(conn)
          conNum += 1
        }
      }
      pool.poll()
    })
  }

  //释放连接
  def releaseConn(conn: Connection): Unit = {
    pool.push(conn)
  }

  //加载驱动
  @tailrec
  private def preGetConn(): Unit = {
    //控制加载
    if (conNum < max && !pool.isEmpty) {
      println("Jdbc Pool has no connection now, please wait a moments!")
      Thread.sleep(2000)
      preGetConn()
    } else {
      Class.forName("com.mysql.cj.jdbc.Driver")
    }
  }

}
