import com.typesafe.config.ConfigFactory
import akka.actor.{ Props, Actor, ActorSystem }
import akka.kernel.Bootable
import com.basho.riak.client._
import java.util.UUID
import scala.collection.JavaConversions._
import java.util.Date
import com.mongodb.Mongo

case class Msg(id: String, date: Date, msg: String)
//#actor
class SloggerActor extends Actor {
  val riakClient = RiakFactory.pbcClient //httpClient()

  // create a new bucket
  val myBucket = riakClient.createBucket("slogger").execute();
  val mongo = new Mongo()
  val db = mongo.getDB( "slogger" );
  val coll = db.getCollection("logs")
  def receive = {
    case m: Msg => {
      printf("[%s] - %s\n", m.id, m.msg)
      try {
        val data = myBucket.store(m.date.toString, m.msg).returnBody(true).execute();
        //    		println(data.getValueAsString)
        //    		println("Keys = "+myBucket.keys().mkString)
        //    		myBucket.keys().foreach(key => println(myBucket.fetch(key).execute.getValueAsString))
      } catch {
        case e: Throwable => e.printStackTrace
      }
    }
    case _ => println("unknow msg received.")
  }

  /*
   * 
   * 
   *  // add data to the bucket
    myBucket.store("key1", "value1").execute();

    //fetch it back
    var myData = myBucket.fetch("key1").execute();

    // you can specify extra parameters to the store operation using the
    // fluent builder style API
    myData = myBucket.store("key1", "value2").returnBody(true).execute();

    // delete
    myBucket.delete("key1").rw(3).execute();
   */
}
//#actor

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