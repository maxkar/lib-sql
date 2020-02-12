package ru.maxkar.lib.sql

import java.sql.Connection
import javax.sql.DataSource

/**
 * A manager defining access discipline to a database.
 */
trait ConnectionManager {
  /**
   * Invokes the cb on an active database connection.
   *
   * The callback should assume that the provided connection instance is valid
   * only during the callback execution. The connection object should not be
   * stored in any variables or passed to other threads.
   *
   * The callback should not attempt to close the underlying connection.
   *
   * @param cb callback using the database connection.
   * @return result returned from cb.
   */
  def withConnection[T](cb : Connection => T): T
}


object ConnectionManager {
  /**
   * Creates a connection manager that provides single-threaded
   * non-reenterant access to the {{{conn}}} instance.
   *
   * @param conn connection being passed to all callbacks.
   */
  def apply(conn: Connection): ConnectionManager =
    new ConnectionConnectionManager(conn)


  /**
   * Creates a pool-based connection manager.
   * The manager delegates all resource management to the {{{ds}}}
   * instance and works like an API bridge.
   *
   * @param ds connection pool implementation.
   */
  def apply(ds: DataSource): ConnectionManager =
    new DatasourceConnectionManager(ds)
}
