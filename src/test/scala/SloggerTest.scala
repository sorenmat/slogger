import akka.actor.ActorRef
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorContext, TypedActor, TypedProps }
import com.typesafe.config.ConfigFactory
import akka.actor.{ Props, Actor, ActorSystem }
import akka.kernel.Bootable
import com.basho.riak.client._
import java.util.UUID
import scala.collection.JavaConversions._ 
import java.util.Date

object SloggerTest {

  def start {
    println("Starting server")
	  val server = new SloggerServer
	  println("Starting server - Done")
    println("Starting......")
    val system = ActorSystem("sloggerclient",  ConfigFactory.load.getConfig("sloggerclient"))
    
   val  remoteActor = system.actorFor("akka://SloggerServer@127.0.0.1:2552/user/sloggerActor")
   for(i <- 0 to 100)
    remoteActor ! Msg("id", new java.util.Date, "Hello world "+i+" !!!")
    Thread.sleep(1000)
    
    println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
      val riakClient = RiakFactory.pbcClient //httpClient()

    // create a new bucket
    val myBucket = riakClient.createBucket("slogger").execute();
   
 		myBucket.keys().foreach(key => println(key+" == "+myBucket.fetch(key).execute.getValueAsString))
    system.shutdown
    server.shutdown
  }
  
  def main(args: Array[String]) {
    SloggerTest.start
  }
}