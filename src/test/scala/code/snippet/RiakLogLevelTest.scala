package code.snippet

import com.mongodb
import com.mongodb._
import xml.NodeSeq

/**
 * Created with IntelliJ IDEA.
 * User: soren
 * Date: 8/19/12
 * Time: 8:45
 * To change this template use File | Settings | File Templates.
 */
object RiakLogLevelTest {
  def main(args: Array[String]) {
    val mongo = new Mongo()
    val db = mongo.getDB("slogger");
    val coll = db.getCollection("logs")


    val levelCount = """function () {
      emit(this.level, 1);
    }"""

    val reduceCount = """function (k, vals) {
                            var sum = 0;
                            for (var i in vals) {
                                sum += vals[i];
                            }
                            return sum;
                        }"""



    val query = new BasicDBObject();
    query.put("server", "Srens-MacBook-Pro.local");

    val mapCommand = new mongodb.MapReduceCommand(coll, levelCount, reduceCount, null, MapReduceCommand.OutputType.INLINE, query)

    val result = coll.mapReduce(mapCommand)
    println(result.getCommandResult)
    //coll.gr
    val servers = coll.distinct("server")
    println(servers)
  }
}
