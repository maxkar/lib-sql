package ru.maxkar.lib.sql

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet


/**
 * A query to a database. Each query could be characterized by the query text
 * with potential placeholders and corresponding argument values for each
 * generated placehodler.
 *
 * @param fragments textual fragments of the query.
 * @param subqueries nested query fragments.
 */
final class Query(fragments: Seq[String], subqueries: Seq[QueryFragment]) extends QueryFragment {

  /**
   * Invokes a callback on a statement prepared for the given connection.
   *
   * The prepared statement is created based on a text of this query and
   * all the parameters are set.
   *
   * @tparam T type of query result.
   * @param conn connection to use.
   * @param cb callback to invoke on the prepared statement.
   */
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


  /**
   * Generates a query text with placeholders.
   */
  def generateQuery(): String = {
    val sb = new StringBuilder()
    appendQueryText(sb)
    sb.toString
  }


  /**
   * Runs a database update on the given connection.
   * @param conn connection to a database to update.
   * @return number of rows updated.
   */
  def update(conn: Connection): Int =
    withPreparedStatement(conn) { ps =>
      ps.executeUpdate()
    }


  /**
   * Runs a database update on a managed connection.
   * @param connMgr manager of database connections.
   * @return number of rows updated.
   */
  def update()(implicit connMgr: ConnectionManager): Int =
    connMgr.withConnection(conn => update(conn))



  /**
   * Extracts (reads, selects) data using the query.
   *
   * The code prepares a statement, initializes all arguments, executes
   * query and invokes the callback function.
   *
   * @tparam T type of the data being extracted.
   * @param conn connection to a database to execute the query on.
   * @param conn connMgr manager of database connections.
   * @param callback data extraction callback.
   * @return data extracted from the connection.
   */
  def select[T](conn: Connection, callback: ResultSet => T): T =
    withPreparedStatement(conn) { ps =>
      val rs = ps.executeQuery()
      try {
        callback(rs)
      } finally {
        rs.close()
      }
    }



  /**
   * Extracts (reads, selects) data using this query.
   *
   * The code prepares a statement, initializes all arguments, executes
   * query and invokes the callback function.
   *
   * @tparam T type of the data being extracted.
   * @param callback data extraction callback.
   * @param connMgr manager of database connections.
   * @return data extracted from the connection.
   */
  def select[T](
        callback: ResultSet => T)(
        implicit connMgr: ConnectionManager)
      : T =
    connMgr.withConnection(conn => select(conn, callback))


  override def setParameters(ps: PreparedStatement, startIdx: Int): Int = {
    var idx = startIdx
    val subqueryIter = subqueries.iterator

    while (subqueryIter.hasNext)
      idx = subqueryIter.next.setParameters(ps, idx)

    return idx
  }


  override def appendQueryText(sb: StringBuilder): Unit = {
    val fragIter = fragments.iterator
    val subqueryIter = subqueries.iterator
    sb ++= fragIter.next
    while (subqueryIter.hasNext) {
      subqueryIter.next.appendQueryText(sb)
      sb ++= fragIter.next
    }
  }
}
