package ru.maxkar.lib.sql

import java.sql.PreparedStatement

trait SqlParameter {
  def appendQueryText(sb: StringBuilder): Unit
  def setParameters(ps: PreparedStatement, idx: Int): Int
}

