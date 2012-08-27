import com.mongodb
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


    val levelCount = "function () { if (!this.level) { return; } emit(this.level, 1); }"

    val reduceCount = "function (k, vals) { var sum = 0; for (var i in vals) { sum += vals[i]; } return sum; }"

    /*
 function() {
   emit(this.type, 1);
 },
 function(k, vals) {
   var total = 0;
   for (var i = 0; i < vals.length; i++)
   total += vals[i];
   return total;
 }   */
    val query = new BasicDBObject();
    query.put("server", "Srens-MacBook-Pro.local");

    val mapCommand = new mongodb.MapReduceCommand(coll, levelCount, reduceCount, "levels1", MapReduceCommand.OutputType.REPLACE, query)

    val result = coll.mapReduce(mapCommand)
    println(result.getCommandResult)
    //coll.gr
    val servers = db.getCollection("levels")
    val c = servers.find()

    while (c.hasNext)  {
      println(c.next())
    }

  }
}
