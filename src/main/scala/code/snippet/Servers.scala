package code.snippet

import xml.{Text, NodeSeq}
import net.liftweb.util.Helpers._
import com.mongodb.Mongo
import scala.collection.JavaConversions._

class Servers {

  val mongo = new Mongo()
  val db = mongo.getDB( "slogger" );
  val coll = db.getCollection("logs")

  def render(xhtml: NodeSeq) = {

       val servers = coll.distinct("server")
       val cursor = coll.find()

       //bind("server", xhtml, "servers" -> "test")

       coll.find().iterator().toList.sortWith((o1, o2) => o1.get("time").toString > o2.get("time").toString).flatMap {
         key => bind("foo", xhtml,
           "server" -> {
             if(key.get("server") != null) key.get("server").toString else ""
           },
           "time" -> key.get("time").toString,
           "message" -> key.get("logMessage").toString)

       }

     }
}
