package ru.maxkar.lib.sql

import java.sql.Connection

/**
 * A transaction at some isolation level.
 *
 * @param IL isolation level of the transaction.
 */
final class Transaction[IL] private[sql](
      conn: Connection)
    extends DBConnection {

  @volatile
  private var rollbackOnly = false

  private val lock = new Object()


  /**
   * Marks the transaction to revert all changes instead of saving
   * them at the end of transactional block.
   */
  def setRollbackOnly(): Unit =
    rollbackOnly = true

  /**
   * Checks if the changes should be rolled back instead of commiting.
   */
  def isRollbackOnly(): Boolean =
    rollbackOnly


  override def withConnection[T](cb : Connection => T): T =
    lock synchronized {
      cb(conn)
    }


  override def allOrNothing[T](
      level: Int, cb: Transaction[_] => T): T =
    subTx(cb)


  /**
   * Executes a block in "all-or-nothing" way in a "nested" transaction.
   * The block is guaranteed to either have all updates successfully or
   * none of them. The operation does not affect parent transaction.
   */
  def subTx[T](cb: Transaction[IL] => T): T = {
    val savepoint = conn.setSavepoint()
    try {
      val innerTx = new Transaction[IL](conn)
      val res = cb(this)
      if (innerTx.isRollbackOnly)
        conn.rollback(savepoint)
      res
    } catch {
      case e: Throwable =>
        conn.rollback(savepoint)
        throw e
    } finally {
      conn.releaseSavepoint(savepoint)
    }
  }
}
