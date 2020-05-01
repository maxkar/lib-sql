package ru.maxkar.lib.sql

import java.sql.Connection
import java.util.concurrent.Semaphore

/**
 * An implementation of the connection manager sharing access
 * to exactly one connection.
 */
final class AutocommitConnection(
      conn: Connection)
    extends DBConnection with AutocommitDBContext {
  /**
   * Synchronization primitive for our manager.
   *
   * This class emulates single-connection pool so the synchronization
   * mechanism should be non-reenterant.
   */
  private val mutex = new Semaphore(1)

  override def withConnection[T](cb : Connection => T): T = {
    mutex.acquire()
    try {
      cb(conn)
    } finally {
      mutex.release()
    }
  }
}
