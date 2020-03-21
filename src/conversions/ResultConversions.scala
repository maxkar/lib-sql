package ru.maxkar.lib.sql.conversions

import java.sql.ResultSet
import java.sql.SQLException

import ru.maxkar.lib.sql.ResultSetQuery

/**
 * Utilities for the result set conversions.
 */
object ResultConversions {
  /**
   * Creates a pair of conversions for primitive values.
   * @tparam T type of the object being accessed.
   * @param fetch function used to fetch field from a result set
   *   (ignoring nullability).
   * @return a pair of accessors for non-nullable and nullable fields.
   */
  def primitiveResult[T](
        fetch: (ResultSet, String) => T)
      : (ResultSetQuery => T, ResultSetQuery => Option[T]) = {
    def readOpt(q: ResultSetQuery): Option[T] = {
      val v = fetch(q.rs, q.fieldName)
      if (q.rs.wasNull())
        None
      else
        Some(v)
    }

    def read(q: ResultSetQuery): T = {
      val v = fetch(q.rs, q.fieldName)
      if (q.rs.wasNull())
        throw new SQLException(s"Non-nullable field ${q.fieldName} was null")
      else
        v
    }

    (read, readOpt)
  }


  /**
   * Creates a pair of conversions for object (non-primitive) values.
   * @tparam T type of the object being accessed.
   * @param fetch function used to fetch field from a result set
   *   (ignoring nullability).
   * @return a pair of accessors for non-nullable and nullable fields.
   */
  def refResult[T <: AnyRef](
        fetch: (ResultSet, String) => T)
      : (ResultSetQuery => T, ResultSetQuery => Option[T]) = {
    def readOpt(q: ResultSetQuery): Option[T] = {
      Option(fetch(q.rs, q.fieldName))
    }

    def read(q: ResultSetQuery): T = {
      val v = fetch(q.rs, q.fieldName)
      if (v == null)
        throw new SQLException(s"Non-nullable field ${q.fieldName} was null")
      v
    }

    (read, readOpt)
  }
}
