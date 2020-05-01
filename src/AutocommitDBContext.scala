package ru.maxkar.lib.sql

import java.sql.Connection

/**
 * A database context where each statement is commited after the execution.
 */
trait AutocommitDBContext extends DBContext {
  /**
   * Performs the callback on the "read uncommited" isolation level.
   */
  def withReadUncommittedTx[T](
      cb: Transaction[Isolation.ReadUncommitted] => T): T =
    withTx(Connection.TRANSACTION_READ_UNCOMMITTED, cb)

  /**
   * Performs the callback on the "read committed" isolation level.
   */
  def withReadCommittedTx[T](
      cb: Transaction[Isolation.ReadCommitted] => T): T =
    withTx(Connection.TRANSACTION_READ_COMMITTED, cb)

  /**
   * Performs the callback on the "repeatable read" isolation level.
   */
  def withRepeatableReadTx[T](
      cb: Transaction[Isolation.RepeatableRead] => T): T =
    withTx(Connection.TRANSACTION_REPEATABLE_READ, cb)

  /**
   * Performs the callback on the "serializable" isolation level.
   */
  def withSerializableTx[T](
      cb: Transaction[Isolation.Serializable] => T): T =
    withTx(Connection.TRANSACTION_SERIALIZABLE, cb)


  /**
   * Performs a callback with the given isolation level.
   *
   * @tparam IL compile-time isolation level evidence.
   * @tparam T type of value returned by the callback.
   * @param level numeric JDBC isolation level.
   * @param cb callback to invoke.
   */
  private def withTx[IL, T](level: Int, cb: Transaction[IL] => T): T =
    withConnection { conn =>
      conn.setAutoCommit(false)
      conn.setTransactionIsolation(level)
      try {
        val tx = new Transaction[IL](conn)
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
