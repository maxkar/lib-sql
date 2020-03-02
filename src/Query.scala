package ru.maxkar.lib.sql

import java.sql.Connection
import java.sql.PreparedStatement

final class Query(fragments: Seq[String], params: Seq[SqlParameter]) {
  def withPreparedStatement[T](
      conn: Connection)(cb: PreparedStatement => T): T = {
    val queryText = generateQuery()
    val ps = conn.prepareStatement(queryText)

    try {
      setParameters(ps, 1)
      cb(ps)
    } finally {
      ps.close()
    }
  }


  def setParameters(ps: PreparedStatement, startIdx: Int): Int = {
    var idx = startIdx
    val paramIter = params.iterator

    while (paramIter.hasNext)
      idx = paramIter.next.setParameters(ps, idx)

    return idx
  }


  def generateQuery(): String = {
    val sb = new StringBuilder()
    appendQueryText(sb)
    sb.toString
  }


  def appendQueryText(sb: StringBuilder): Unit = {
    val fragIter = fragments.iterator
    val paramIter = params.iterator
    sb ++= fragIter.next
    while (paramIter.hasNext) {
      paramIter.next.appendQueryText(sb)
      sb ++= fragIter.next
    }
  }

}
