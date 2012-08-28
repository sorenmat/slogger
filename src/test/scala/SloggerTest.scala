import com.scalaprog._
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem

object SloggerTest {

  def start {
    println("Starting server")
	  val server = new SloggerServer
	  println("Starting server - Done")
    println("Starting......")
    val system = ActorSystem("sloggerclient",  ConfigFactory.load.getConfig("sloggerclient"))
    
   val  remoteActor = system.actorFor("akka://SloggerServer@127.0.0.1:2552/user/sloggerActor")
    val key = Key("localhost", System.currentTimeMillis(), "test")
   for(i <- 0 to 1000) {
     remoteActor ! Msg(key, Info(), "There is an error "+i+" !!!")
   }

    Thread.sleep(1000)
    /*
    println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
      val riakClient = RiakFactory.pbcClient //httpClient()

    // create a new bucket
    val myBucket = riakClient.createBucket("slogger").execute();
   
 		myBucket.keys().foreach(key => println(key+" == "+myBucket.fetch(key).execute.getValueAsString))
 		*/
    system.shutdown
    server.shutdown
  }
  
  def main(args: Array[String]) {
    SloggerTest.start
  }
}