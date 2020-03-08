package ru.maxkar.lib.sql

/**
 * Syntax extensions provided by the library.
 */
package object syntax {
  /**
   * SQL string interpolation.
   */
  implicit final class SqlQuerySyntax(val ctx: StringContext) extends AnyVal {
    def sql(params: QueryFragment*): Query =
      new Query(ctx.parts, params)
  }
}
