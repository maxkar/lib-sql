package ru.maxkar.lib.sql

package object syntax {
  implicit final class SqlQuerySyntax(val ctx: StringContext) extends AnyVal {
    def sql(params: SqlParameter*): Query =
      new Query(ctx.parts, params)
  }
}
