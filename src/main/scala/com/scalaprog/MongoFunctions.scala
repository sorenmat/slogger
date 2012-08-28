package com.scalaprog

import com.mongodb.{BasicDBObject, DBCollection}

/**
 * User: soren
 */
object MongoFunctions {

  /**
   * Count the log levels for a specific server
   *
   * @param level
   * @param server
   * @param coll
   * @return Long the number for objects for that log level
   */
  def getCount(level: String, server: String, coll: DBCollection) = {
    val query = new BasicDBObject()
    query.put("server", server)
    query.put("level", level)
    coll.count(query)

  }


}
