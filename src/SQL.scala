package ru.maxkar.lib.sql

import java.sql.BatchUpdateException

/**
 * Some nice SQL utilities.
 */
object SQL {
  def batchUpdate(
        batchSize: Int = 1024)(
        queries: Iterable[Query])(
        implicit ctx: DBContext)
      : Seq[Long] = {
    val itr = queries.iterator
    if (!itr.hasNext)
      return Seq.empty

    var query = itr.next
    var text = query.generateQuery()
    var aggSize = 1
    val res = new scala.collection.mutable.ArrayBuffer[Long]
    ctx.withConnection { conn =>
      var ps = conn.prepareStatement(text)
      try {
        /* First item. */
        query.setParameters(ps, 1)
        ps.addBatch()
        /* Process rest. */
        while (itr.hasNext) {
          query = itr.next
          val newText = query.generateQuery()
          if (newText != text) {
            res ++= ps.executeLargeBatch()
            text = newText
            ps.close()
            ps = conn.prepareStatement(newText)
            aggSize = 1
          } else if (aggSize >= batchSize) {
            res ++= ps.executeLargeBatch()
            aggSize = 1
          } else {
            aggSize += 1
          }
          query.setParameters(ps, 1)
          ps.addBatch()
        }
        /* Flush the data. */
        res ++= ps.executeLargeBatch()
      } catch {
        case e: BatchUpdateException =>
          throw new BatchException(text, res.toSeq, e)
      } finally {
        ps.close()
      }
    }
    res.toSeq
  }
}
