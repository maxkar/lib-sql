package ru.maxkar.lib.sql

/**
 * A standard hierarchy of transaction isolation levels.
 *
 * The levels are not expected to be instantiated. Their only use is to
 * serve as type bounds in the Transaction class.
 */
object Isolation {
  abstract class ReadUncommitted
  abstract class ReadCommitted extends ReadUncommitted
  abstract class RepeatableRead extends ReadCommitted
  abstract class Serializable extends RepeatableRead
}
