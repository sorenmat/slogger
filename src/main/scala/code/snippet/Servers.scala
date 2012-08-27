package code.snippet

import xml.{Text, NodeSeq}
import com.mongodb.{BasicDBObject, Mongo}
import scala.collection.JavaConversions._

import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util.Helpers._

class Servers {

  val mongo = new Mongo()
  val db = mongo.getDB("slogger");
  val coll = db.getCollection("logs")

  def render(xhtml: NodeSeq): NodeSeq = {
    val servers = coll.distinct("server")

    servers.map(s => {
      // build query
      val query = new BasicDBObject()
      query.put("server", s)
      val result = coll.find(query)
      while(result.hasNext) {
        val obj = result.next()
        println(obj.get("level"))
      }

    })

    val data = servers.toList.flatMap({
      key => bind("foo", xhtml,
        /*"server" -> link("/server?name=" + key.toString, () => {
          println(key.toString)
        }, Text(key.toString)))
    })
    */
        "server" -> clickableImage(xhtml, "/server.html?name=" + key.toString, "images/server.png", key.toString))
    })


    data
  }

  def clickableImage(xhtml: NodeSeq, url: String, image: String, title: String): NodeSeq = {
    <a class="btn btn-large" href={ url } title={ title }><img src={ image } height="48" width="48"/><br/>{title}</a>
  }
}
