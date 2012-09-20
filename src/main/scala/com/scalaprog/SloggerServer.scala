package com.scalaprog

import akka.actor.{Props, Actor, ActorSystem}
import akka.kernel.Bootable
import com.mongodb.{BasicDBObject, Mongo}
import akka.util.Duration
import akka.util.duration._
import java.net.InetAddress
import management.ManagementFactory


case class Error(value: String = "Error") extends Level(value)

case class Warn(value: String = "Warn") extends Level(value)

case class Info(value: String = "Info") extends Level(value)

case class Debug(value: String = "Debug") extends Level(value)

case class Trace(value: String = "Trace") extends Level(value)


case class Key(server: String, var date: Long, context: String)

// Log message
case class Msg(id: Key, level: Level, msg: String)

//#actor
class SloggerActor extends Actor {

  val mongo = new Mongo()
  val db = mongo.getDB("slogger");
  val coll = db.getCollection("logs")
  val metrics = db.getCollection("metrics")

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
    case c: CPUMetric => {
      val info = new BasicDBObject();
      info.put("server", c.key.server)
      info.put("time", c.key.date.toString)
      info.put("context", c.key.context)
      info.put("cpuload", c.load)
      metrics.save(info)
    }

    case _ => {
      println("unknow msg received...")
    }
  }

}

class SloggerServer extends Bootable {
  val system = ActorSystem("SloggerServer")
  val actor = system.actorOf(Props[SloggerActor], "sloggerActor")
  //val cpuactor = system.actorOf(Props[SloggerCPUActor], "sloggerCPUActor")

  val period = 1000 milliseconds

  system.scheduler.schedule(Duration.Zero, 1000 milliseconds)({ actor ! CPUMetric(ManagementFactory.getOperatingSystemMXBean.getSystemLoadAverage) })
  //val cancellable = system.scheduler.schedule(0 milliseconds, 50 milliseconds, cpuactor, new CPUMetric())
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