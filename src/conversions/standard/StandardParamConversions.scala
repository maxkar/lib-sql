package ru.maxkar.lib.sql.conversions.standard

import ru.maxkar.lib.sql.conversions.Parameters.parameterConversions

/**
 * Standard parameter conversions common across all databases.
 */
trait StandardParamConversions {
  implicit val (int2Query, optInt2Query) =
    parameterConversions[Int](_.setInt(_, _))

  implicit val (long2Query, optLong2Query) =
    parameterConversions[Long](_.setLong(_, _))

  implicit val (boolean2Query, optBoolean2Query) =
    parameterConversions[Boolean](_.setBoolean(_, _))

  implicit val (double2Query, optDouble2Query) =
    parameterConversions[Double](_.setDouble(_, _))

  implicit val (string2Query, optString2Query) =
    parameterConversions[String](_.setString(_, _))
}
