package com

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import ch.qos.logback.core.status.Status
import akka.actor.ActorSystem
import scalaprog._
import com.typesafe.config.ConfigFactory
import akka.actor.ActorRef
import java.net.InetAddress
import scalaprog.Debug
import scalaprog.Error
import scalaprog.Info
import scalaprog.Key
import scalaprog.Level
import scalaprog.Msg
import scalaprog.Trace
import scalaprog.Warn

class Log2Slogger[E <: ILoggingEvent] extends AppenderBase[E] {

  var remoteActor: ActorRef = null
  val host = InetAddress.getLocalHost.getHostName

  override def start {
    super.start
    println("Starting......")
    try {
      val system = ActorSystem("sloggerclient")
      remoteActor = system.actorFor("akka://SloggerServer@127.0.0.1:2552/user/sloggerActor")

    } catch {
      case e: Throwable => e.printStackTrace
    }

  }

  override def append(eventObject: E) {
    val key = Key(host, System.currentTimeMillis(), eventObject.getLoggerContextVO.getName)
    val level: Level = eventObject.getLevel.levelStr match {
      case "ERROR" => Error()
      case "WARN" => Warn()
      case "INFO" => Info()
      case "DEBUG" => Debug()
      case "TRACE" => Trace()
      case _ => Debug()
    }
    remoteActor ! Msg(key, level, eventObject.getMessage())
  }

  override def addError(err: String) {
    val key = Key(host, System.currentTimeMillis(), "")
    remoteActor ! Msg(key, Error(), err)
  }

  override def isStarted = true

  override def addStatus(s: Status) {
    val key = Key(host, System.currentTimeMillis(), "")
    val statusLevel = s.getLevel
    val message = if (s != null) s.getMessage() else ""
    remoteActor ! Msg(key, Info(), message)
  }
}

