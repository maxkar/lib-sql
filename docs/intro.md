# Lib-sql - Simple SQL Library

The library provides Scala API over the standard JDBC layer.

Library assumptions:

 * Applications using the library rely on a fixed database schema. 
 * The library does not target any specific database engine.
 * Developers using the library know SQL.
 * It is easy to read and write code using the library.
 * Developers don't have to spend much time learning the library.
 * The library spots when a parameter of unsupported type is passed to a query.
 * Set of types supported by the library is extensible.
 * The library does not validate SQL syntax before query is sent for execution.
 * The library is secure. 
