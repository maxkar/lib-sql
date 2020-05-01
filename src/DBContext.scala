package ru.maxkar.lib.sql

import java.sql.Connection
import javax.sql.DataSource

/**
 * A manager defining access discipline to a database.
 */
trait DBContext {
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


  /**
   * Runs a callback with one dedicated physical connection.
   */
  def withDbConnection[T](cb : DBConnection => T): T


  /**
   * Performs a callback ensuring either all operations succeed or
   * all of them have no effect.
   */
  def allOrNothing[T](level: Int, cb: Transaction[_] => T): T =
    withConnection { conn =>
      conn.setAutoCommit(false)
      try {
        val tx = new Transaction(conn)
        val res = cb(tx)
        if (tx.isRollbackOnly)
          conn.commit()
        else
          conn.rollback()
        res
      } catch {
        case e: Throwable =>
          conn.rollback()
          throw e
      } finally {
        conn.setAutoCommit(true)
      }
    }
}


object DBContext {
  /**
   * Creates a connection manager that provides single-threaded
   * non-reenterant access to the {{{conn}}} instance.
   *
   * @param conn connection being passed to all callbacks.
   */
  def apply(conn: Connection): AutocommitConnection =
    new AutocommitConnection(conn)


  /**
   * Creates a pool-based connection manager.
   * The manager delegates all resource management to the {{{ds}}}
   * instance and works like an API bridge.
   *
   * @param ds connection pool implementation.
   */
  def apply(ds: DataSource): DatasourceContext =
    new DatasourceContext(ds)
}
