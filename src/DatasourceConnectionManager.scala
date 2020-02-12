package ru.maxkar.lib.sql

import javax.sql.DataSource
import java.sql.Connection

/**
 * An implementation of the connection manager relying on connection pool.
 */
private final class DatasourceConnectionManager(
      ds: DataSource)
    extends ConnectionManager {

  override def withConnection[T](cb : Connection => T): T = {
    val conn = ds.getConnection()
    try {
      cb(conn)
    } finally {
      conn.close()
    }
  }
}

