package ru.maxkar.lib.sql.conversions.standard

import ru.maxkar.lib.sql.ResultSetQuery
import ru.maxkar.lib.sql.conversions.ResultConversions._

import java.sql.SQLException

/**
 * Standard conversions common across all databases.
 */
trait StandardResultConversions {
  implicit val (rsQuery2Int, optRsQuery2Int) =
    primitiveResult[Int](_.getInt(_))

  implicit val (rsQuery2Long, optRsQuery2Long) =
    primitiveResult[Long](_.getLong(_))

  implicit val (rsQuery2Boolean, optRsQuery2Boolean) =
    primitiveResult[Boolean](_.getBoolean(_))

  implicit val (rsQuery2Double, optRsQuery2Double) =
    primitiveResult[Double](_.getDouble(_))

  implicit val (rsQuery2String, optRsQuery2String) =
    refResult[String](_.getString(_))
}
