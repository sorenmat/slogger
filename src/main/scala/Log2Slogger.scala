import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.AppenderBase
import ch.qos.logback.core.status.Status
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.Props
import akka.actor.ActorRef

class Log2Slogger[E <: ILoggingEvent] extends AppenderBase[E] {

  var remoteActor: ActorRef = null
  override def start {
	super.start
    println("Starting......")
	try {
		val system = ActorSystem("sloggerclient", ConfigFactory.load.getConfig("sloggerclient"))
				remoteActor = system.actorFor("akka://SloggerServer@127.0.0.1:2552/user/sloggerActor")
	  
	} catch {
	  case e: Throwable => e.printStackTrace
	}

  }
  override def append(eventObject: E) {
    remoteActor ! Msg("id", new java.util.Date, eventObject.getMessage())
  }

  override def addError(err: String) {
     remoteActor ! Msg("id",new java.util.Date, err)
  }

  override def isStarted = true

  override def addStatus(s: Status) {
    println
    remoteActor ! Msg("id", new java.util.Date,s.getMessage())
  }
}

