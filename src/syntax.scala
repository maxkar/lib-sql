package ru.maxkar.lib.sql

import java.sql.ResultSet
import java.sql.SQLException

import scala.collection.mutable.ArrayBuffer


/**
 * Syntax extensions provided by the library.
 */
package object syntax {
  /**
   * SQL string interpolation.
   */
  implicit final class SqlQuerySyntax(val ctx: StringContext) extends AnyVal {
    def sql(params: QueryFragment*): Query =
      new Query(ctx.parts, params)
  }


  /**
   * Creates a multi-row parser based on a single-row one.
   * @param rowParser parser for a single row.
   * @return parser for the whole result set.
   */
  def many[T](rowParser: ResultSet => T): ResultSet => Seq[T] =
    rs => {
      val res = new ArrayBuffer[T]
      while (rs.next())
        res += rowParser(rs)
      res.toSeq
    }


  /**
   * Creates a multi-row parser based on a single-row one.
   * The resulting parser will throw an SQLException if the result
   * set is empty.
   * @param rowParser parser for a single row.
   * @return parser for the whole result set.
   */
  def atLeastOne[T](rowParser: ResultSet => T): ResultSet => Seq[T] =
    rs => {
      if (!rs.next())
        throw new SQLException("Expected at least 1 row but got none")

      val res = new ArrayBuffer[T]
      res += rowParser(rs)

      while (rs.next())
        res += rowParser(rs)
      res.toSeq
    }


  /**
   * Fetches exactly one element from result set (and ensures there are
   * no extra elements).
   *
   * @param rowParser parser for a single row.
   * @return parser for entire result set ensuring there is exactly one row.
   */
  def one[T](rowParser: ResultSet => T): ResultSet => T =
    rs => {
      if (!rs.next())
        throw new SQLException("Expected 1 row but got none")

      val res = rowParser(rs)

      if (rs.next())
        throw new SQLException("Expected 1 row but got at least 2")
      res
    }


  /**
   * Fetches at most one element from result set (and ensures there are
   * no extra elements).
   *
   * @param rowParser parser for a single row.
   * @return parser for entire result set ensuring there is at most one row.
   */
  def optional[T](rowParser: ResultSet => T): ResultSet => Option[T] =
    rs => {
      if (!rs.next())
        None
      else {
        val res = rowParser(rs)
        if (rs.next())
          throw new SQLException("Expected at most 1 row but got at least 2")
        Some(res)
      }
    }
}
