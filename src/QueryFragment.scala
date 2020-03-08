package ru.maxkar.lib.sql

import java.sql.PreparedStatement

/**
 * A fragment of an SQL query.
 *
 * Each fragment is characterized by the query text (which may contain
 * placeholders) and corresponding arguments to be used for every placeholder.
 */
trait QueryFragment {
  /**
   * Appends the query text to the accumulator.
   * @param sb builder to append the text to.
   */
  def appendQueryText(sb: StringBuilder): Unit

  /**
   * Sets values of some placeholders on the prepared statement.
   * @param ps prepared statement to set parameters on.
   * @param idx index of the first parameter to be set.
   * @return index of the next parameter to be set.
   */
  def setParameters(ps: PreparedStatement, idx: Int): Int
}

