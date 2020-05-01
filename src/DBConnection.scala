package ru.maxkar.lib.sql

/**
 * An active (established) physical connection to a database.
 *
 * General DBContext is allowed to establish a new connection for
 * every call to withConnection method. The general context could also
 * use connection pool. All this management and sharing may be unnecessary
 * for the code performing multiple queries one after each other. The
 * DBConnection should be used by such methods instead of DBContext.
 */
trait DBConnection extends DBContext {
  override def withDbConnection[T](cb : DBConnection => T): T =
    cb(this)
}
