package ru.maxkar.lib.sql

import scala.language.implicitConversions
import java.sql.PreparedStatement

package object conversions {

  implicit def int2SqlParameter(v: Int): SqlParameter =
    new SqlParameter() {
      override def appendQueryText(sb: StringBuilder): Unit =
        sb += '?'
      override def setParameters(ps: PreparedStatement, idx: Int): Int = {
        ps.setInt(idx, v)
        return idx + 1
      }
    }

  implicit def str2SqlParameter(v: String): SqlParameter =
    new SqlParameter() {
      override def appendQueryText(sb: StringBuilder): Unit =
        sb += '?'
      override def setParameters(ps: PreparedStatement, idx: Int): Int = {
        ps.setString(idx, v)
        return idx + 1
      }
    }


  implicit def query2Parameter(q: Query): SqlParameter =
    new SqlParameter() {
      override def appendQueryText(sb: StringBuilder): Unit =
        q.appendQueryText(sb)
      override def setParameters(ps: PreparedStatement, idx: Int): Int =
        q.setParameters(ps, idx)
    }
}
