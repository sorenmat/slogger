package com.scalaprog

import com.mongodb.{BasicDBObject, DBCollection}
import java.util.regex.Pattern

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

  /**
   * Do a regular expression search of the logMessages
   * @param search
   * @param coll
   * @return
   */
  def searchLogMessages(search: String, coll: DBCollection) = {
    val patrn = Pattern.compile(search, Pattern.CASE_INSENSITIVE);
    val query = new BasicDBObject("logMessage", patrn);

    // finds all people with "name" matching /joh?n/i
    println("Query: "+query)
    val result  = coll.find(query)
    result
  }

}
