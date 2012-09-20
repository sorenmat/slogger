import com.mongodb._
import com.scalaprog.MongoFunctions

/**
 * User: soren
 */
object DistinctLogLevelTest {
  def main(args: Array[String]) {
    val mongo = new Mongo()
    val db = mongo.getDB("slogger");
    val coll = db.getCollection("logs")

    val start = System.currentTimeMillis()
    println("Found " + getCount("Info", coll) + " infos...")
    println("Found " + getCount("Error", coll) + " error...")


    val textstart = System.currentTimeMillis()
    val result = MongoFunctions.searchLogMessages("GET", coll)
    val textstop = System.currentTimeMillis()
    println("regexp query took: "+(textstop-textstart)+" ms")
    while(result.hasNext)
    println(result.next().toString)

    println("queries took: "+(System.currentTimeMillis()-start)+" ms")
  }

  def getCount(level: String, coll: DBCollection) = {
    val query = new BasicDBObject()
    query.put("server", "localhost")
    query.put("level", level)
    coll.count(query)

  }
}
