package code.snippet

import xml.NodeSeq
import net.liftweb.util.Helpers._
import com.mongodb.{BasicDBObject, Mongo}
import scala.collection.JavaConversions._
import net.liftweb.http.S
import net.liftweb.common.Logger

class Server {

  val mongo = new Mongo()
  val db = mongo.getDB("slogger");
  val coll = db.getCollection("logs")

  def render(xhtml: NodeSeq) = {
    val serverName = S.param("name")
    Logger(classOf[Server]).info("ServerName: " + serverName)

    // build query
    val query = new BasicDBObject();
    query.put("server", serverName.get);

    val result = coll.find(query)

    result.iterator().toList.sortWith((o1, o2) => o1.get("time").toString > o2.get("time").toString).flatMap {
      key => bind("foo", xhtml,
        "server" -> {
          //if (key.get("server") != null) key.get("server").toString else ""
          ""
        },
        "time" -> {
          val time = key.get("time").toString
          if (time != null)
            new java.util.Date(time.toLong).toString
          else
            time
        },
        "message" -> key.get("logMessage").toString)

    }

  }
}
