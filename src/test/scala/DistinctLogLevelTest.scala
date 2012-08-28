import com.mongodb._

/**
 * Created with IntelliJ IDEA.
 * User: soren
 * Date: 8/19/12
 * Time: 8:45
 * To change this template use File | Settings | File Templates.
 */
object DistinctLogLevelTest {
  def main(args: Array[String]) {
    val mongo = new Mongo()
    val db = mongo.getDB("slogger");
    val coll = db.getCollection("logs")

    val start = System.currentTimeMillis()
    println("Found " + getCount("Info", coll) + " infos...")
    println("Found " + getCount("Error", coll) + " error...")

    println("queries took: "+(System.currentTimeMillis()-start)+" ms")
  }

  def getCount(level: String, coll: DBCollection) = {
    val query = new BasicDBObject()
    query.put("server", "localhost")
    query.put("level", level)
    coll.count(query)

  }
}
