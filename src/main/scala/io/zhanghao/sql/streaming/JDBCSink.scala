package io.zhanghao.sql.streaming

import java.sql.{Connection, ResultSet, Statement}
import java.util.Properties

import org.apache.spark.sql.{ForeachWriter, Row}

import scala.util.{Failure, Success, Try}

/**
  * 处理从StructuredStreaming中向MySQL中写入数据
  *
  * @author 2019/03/27 zhanghao
  */
class JDBCSink(url: String, username: String, password: String, processType: String) extends ForeachWriter[Row] {

  private var statement: Statement = _
  private var resultSet: ResultSet = _
  private var connection: Connection = _


  override def open(partitionId: Long, epochId: Long): Boolean = {
    Try {
      Class.forName("com.mysql.cj.jdbc.Driver")
      connection = new MySQLPoor(url, username, password).getJdbcConn
      statement = connection.createStatement()
    } match {
      case Success(_) => true
      case Failure(exception) =>
        exception.printStackTrace()
        false
    }
  }

  //处理的主要业务逻辑
  override def process(value: Row): Unit = {
    //TOTAL_PV, IP_PV, SEARCH_ENGINE_PV, KEYWORD_PV, AGENT_PV
    processType match {
      case "TOTAL_PV" =>
        val totalPv = value.getAs[Long]("count")
        val query = "select total_pv from total_pv"
        resultSet = statement.executeQuery(query)
        if (resultSet!=null) {
          val insert = s"insert into total_pv(total_pv) values($totalPv)"
          statement.execute(insert)
        } else {
          val update = s"update total_pv set total_pv=$totalPv"
          statement.execute(update)
        }
      case "IP_PV" =>
        val ip = value.getAs[String]("ip")
        val pv = value.getAs[Long]("count")
        val query = s"select pv from ip_pv where ip='$ip'"
        resultSet = statement.executeQuery(query)
        if (resultSet!=null) {
          val insert = s"insert into ip_pv(ip,pv) values('$ip','$pv')"
          statement.execute(insert)
        } else {
          val update = s"update ip_pv set pv=$pv where ip='$ip'"
          statement.execute(update)
        }
      case "SEARCH_ENGINE_PV" =>
        val searchEngine = value.getAs[String]("search_engine")
        val pv = value.getAs[Long]("count")
        val query = s"select pv from search_engine_pv where search_engine='$searchEngine'"
        resultSet = statement.executeQuery(query)
        if (resultSet!=null) {
          val insert = s"insert into search_engine_pv(search_engine,pv) values('$searchEngine','$pv')"
          statement.execute(insert)
        } else {
          val update = s"update search_engine_pv set pv=$pv where search_engine='$searchEngine'"
          statement.execute(update)
        }
      case "KEYWORD_PV" =>
        val keyword = value.getAs[String]("keyword")
        val pv = value.getAs[Long]("count")
        val query = s"select pv from keyword_pv where keyword='$keyword'"
        resultSet = statement.executeQuery(query)
        if (resultSet!=null) {
          val insert = s"insert into keyword_pv(keyword,pv) values('$keyword','$pv')"
          statement.execute(insert)
        } else {
          val update = s"update keyword_pv set pv='$pv' where keyword='$keyword'"
          statement.execute(update)
        }
      case "AGENT_PV" =>
        val agent = value.getAs[String]("agent")
        val pv = value.getAs[Long]("count")
        val query = s"select pv from agent_pv where agent='$agent'"
        resultSet = statement.executeQuery(query)
        if (resultSet!=null) {
          val insert = s"insert into agent(agent,pv) values('$agent','$pv')"
          statement.execute(insert)
        } else {
          val update = s"update agent_pv set pv='$pv' where agent='$agent'"
          statement.execute(update)
        }
      case _ =>
    }
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
