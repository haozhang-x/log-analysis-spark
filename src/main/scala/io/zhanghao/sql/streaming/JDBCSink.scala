package io.zhanghao.sql.streaming

import java.sql.{Connection, ResultSet, Statement}

import org.apache.spark.sql.ForeachWriter

/**
  * 处理从StructuredStreaming中向MySQL中写入数据
  *
  * @author 2019/03/27 zhanghao
  */
class JDBCSink(url: String, username: String, password: String) extends ForeachWriter {

  private var statement: Statement = _
  private var resultSet: ResultSet = _
  private var connection: Connection = _

  override def open(partitionId: Long, epochId: Long): Boolean = {
    connection = new MySQLPoor(url, username, password).getJdbcConn
    statement = connection.createStatement()
    true
  }

  //处理的主要业务逻辑
  override def process(value: Nothing): Unit = {

  }

  override def close(errorOrNull: Throwable): Unit = {
    if (statement == null) {
      statement.close()
    }
    if (connection == null) {
      connection.close()
    }

  }
}
