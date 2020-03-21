package ru.maxkar.lib.sql

import java.sql.ResultSet

import scala.language.dynamics

/**
 * A representation of one row in the result set.
 */
final class ResultRow(rs: ResultSet) extends Dynamic {
  /**
   * Creates a query for a specific field name. Database dialects are
   * responsible for conversions from this type into types used inside
   * code.
   * @param fieldName name of a field to access.
   */
  def apply(fieldName: String): ResultSetQuery =
    new ResultSetQuery(rs, fieldName)

  /**
   * Syntax extension for accessing field in a regular way.
   */
  def selectDynamic(fieldName: String): ResultSetQuery =
    apply(fieldName)
}
