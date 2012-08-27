package com.scalaprog
import akka.actor.{Props, Actor, ActorSystem}
import akka.kernel.Bootable
import com.mongodb.{BasicDBObject, Mongo}



case class Error(value: String = "Error") extends Level(value)
case class Warn(value: String = "Warn") extends Level(value)
case class Info(value: String = "Info") extends Level(value)
case class Debug(value: String = "Debug") extends Level(value)
case class Trace(value: String = "Trace") extends Level(value)


case class Key(server: String, date: Long, context: String)

// Log message
case class Msg(id: Key, level: Level, msg: String)

//#actor
class SloggerActor extends Actor {

  val mongo = new Mongo()
  val db = mongo.getDB("slogger");
  val coll = db.getCollection("logs")

  var handled = 0

  def receive = {
    case m: Msg => {
      //printf("[%s] - %s\n", m.id, m.msg)
      try {

        val info = new BasicDBObject();
        info.put("server", m.id.server)
        info.put("time", m.id.date.toString)
        info.put("context", m.id.context)
        info.put("level", m.level.code)
        info.put("logMessage", m.msg)
        coll.save(info)

        handled = handled + 1
        if (handled % 100 == 0)
          println("Handled " + handled)
      } catch {
        case e: Throwable => e.printStackTrace
      }
    }
    case _ => println("unknow msg received.")
  }

}

class SloggerServer extends Bootable {
  val system = ActorSystem("SloggerServer")
  val actor = system.actorOf(Props[SloggerActor], "sloggerActor")
  //#setup

  def startup() {

  }

  def shutdown() {
    system.shutdown()
  }
}

object SloggerServer {
  def main(args: Array[String]) {
    new SloggerServer
    println("Started Slogger Application - waiting for messages")
  }
}