package ru.maxkar.lib.sql

import java.sql.ResultSet

/**
 * A "single-field" query to a result set.
 */
final class ResultSetQuery(val rs: ResultSet, val fieldName: String)
