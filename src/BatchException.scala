package ru.maxkar.lib.sql

import java.sql.SQLException
import java.sql.BatchUpdateException

final case class BatchException(
      queryText: String, successUpdates: Seq[Long], cause: BatchUpdateException)
    extends SQLException(
      "Failed to execute batch update call " + queryText + ":" + cause
    ) {
}
