package ru.maxkar.lib.sql.conversions

import java.sql.PreparedStatement

import ru.maxkar.lib.sql.QueryFragment

/**
 * Utilities for parameter conversions.
 */
object Parameters {
  /** A query fragment for setting Sql NULL value. */
  val nullSetter: QueryFragment = new QueryFragment() {
    override def appendQueryText(sb: StringBuilder): Unit =
      sb ++= "NULL"

    override def setParameters(ps: PreparedStatement, idx: Int): Int =
      idx
  }

  /**
   * Creates a query fragment with a single placeholder which
   * value is set by the provided callback.
   *
   * @param cb callback function for setting argument. The callback
   *   takes a statement to configure and parameter index to set.
   * @returns query fragment evaluating and setting one placeholder.
   */
  def singleParameter(cb: (PreparedStatement, Int) => Unit): QueryFragment =
    new QueryFragment() {
      override def appendQueryText(sb: StringBuilder): Unit =
        sb += '?'

      override def setParameters(ps: PreparedStatement, idx: Int): Int = {
        cb(ps, idx)
        return idx + 1
      }
    }


  /**
   * Generates conversions from T and Option[T] into QueryFragment.
   *
   * @tparam T type of the value being converted.
   * @param setter function used to set one parameter on a prepared statement.
   *   The function takes statement, parameter index and value to set.
   * @returns pair of conversion from non-optional and optional (nullable) values.
   */
  def parameterConversions[T](
        setter: (PreparedStatement, Int, T) => Unit)
      : (T => QueryFragment, Option[T] => QueryFragment) = {
    def valueSetter(value: T): QueryFragment =
      singleParameter(setter(_, _, value))
    def optValueSetter(value: Option[T]): QueryFragment =
      value match {
        case None => nullSetter
        case Some(value) => valueSetter(value)
      }
    (valueSetter, optValueSetter)
  }
}
