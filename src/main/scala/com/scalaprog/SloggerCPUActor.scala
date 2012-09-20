package com.scalaprog

import akka.actor.{Props, Cancellable, ActorSystem, Actor}
import com.mongodb.BasicDBObject
import akka.util.duration._
import java.net.InetAddress
import management.{ManagementFactory, OperatingSystemMXBean}

class SloggerCPUActor extends Actor {


  val interval = 1000
  //private val logger = LoggerFactory.getLogger(this.getClass)
  private var scheduledTask: Cancellable = null


  override def preStart() {

    // logger.debug("scheduling a heartbeat to go out every " + interval + " seconds")
    val actor = context.system.actorFor("sloggerActor")
    scheduledTask = context.system.scheduler.schedule(0 milliseconds, 1 seconds, self, CPUMetric(ManagementFactory.getOperatingSystemMXBean.getSystemLoadAverage))
  }

  override def postStop() {
    scheduledTask.cancel
  }

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
      } catch {
        case e: Throwable => e.printStackTrace
      }
    }
    case c: CPUMetric => {
      println("Cpu metrics")

    }
    case _ => println("unknow msg received....")
  }

}
