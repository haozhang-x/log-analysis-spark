package io.zhanghao.sql.streaming

/**
  * JDBCSink process 处理类型的Sink
  *
  * @author 2019/03/28 zhanghao
  */

class ProcessType extends Enumeration{

}

object ProcessType extends Enumeration {
  type PV = Value
  val TOTAL_PV, IP_PV, SEARCH_ENGINE_PV, KEYWORD_PV, AGENT_PV = Value
}
