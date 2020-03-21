package ru.maxkar.lib.sql.dialects

import ru.maxkar.lib.sql.conversions.standard._

/**
 * Standard database dialect - conversions universally applicable
 * to all databases.
 */
object standard
    extends StandardParamConversions
    with StandardResultConversions {
}
