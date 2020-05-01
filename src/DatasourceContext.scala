package ru.maxkar.lib.sql

import javax.sql.DataSource
import java.sql.Connection

/**
 * An implementation of the connection manager relying on connection pool.
 */
final class DatasourceContext(
      ds: DataSource)
    extends AutocommitDBContext {

  override def withDbConnection[T](cb : DBConnection => T): T =
    withConnection(jdbcConn =>
      cb(new AutocommitConnection(jdbcConn))
    )

  override def withConnection[T](cb : Connection => T): T = {
    val conn = ds.getConnection()
    try {
      cb(conn)
    } finally {
      conn.close()
    }
  }
}

