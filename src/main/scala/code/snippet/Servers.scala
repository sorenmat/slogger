package code.snippet

import xml.{Text, NodeSeq}
import com.mongodb.Mongo
import scala.collection.JavaConversions._

import _root_.net.liftweb.util.Helpers._
import com.scalaprog.MongoFunctions

class Servers {

  val mongo = new Mongo()
  val db = mongo.getDB("slogger");
  val coll = db.getCollection("logs")

  def render(xhtml: NodeSeq): NodeSeq = {
    val servers = coll.distinct("server")

    val data = servers.toList.filter(s => s != null).flatMap({
      key => {
        println("key: " + key)
        val infos = MongoFunctions.getCount("Info", key.toString, coll)
        val errors = MongoFunctions.getCount("Error", key.toString, coll)
        bind("foo", xhtml,
          "server" -> clickableImage(xhtml, "/server.html?name=" + key.toString, "images/server.png", key.toString, serverInfo(key.toString, infos, errors)))
      }

    })
    data
  }

  def serverInfo(serverName: String, info: Long, errors: Long): NodeSeq = {
    val result = Text(serverName) ++ <br/> ++
      Text("Errors: " + errors) ++ <br/> ++
      Text("Info: " + info)
    println("Result: " + result.toString)
    result
  }

  def clickableImage(xhtml: NodeSeq, url: String, image: String, title: String, description: NodeSeq): NodeSeq = {
    <div class="btn btn-large" style="width: 200px">
      <a href={url} title={title}>
        <img src={image} height="48" width="48"/>
        <br/>{description}
      </a>
    </div>
  }
}
