package code 
package snippet 

import scala.collection.JavaConversions._
import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._
import com.basho.riak.client._
import org.specs2.internal.scalaz.Digit._0
import com.mongodb.Mongo

class HelloWorld {
 	lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  val mongo = new Mongo()
  val db = mongo.getDB( "slogger" );
  val coll = db.getCollection("logs")

  def howdy = {
  	"#time *" #> {
      val cursor = coll.find()

     val result =  coll.find().iterator().toList.sortWith((o1, o2) => o1.get("time").toString > o2.get("time").toString)map(key => {
       val server = if(key.get("server") != null) key.get("server").toString else ""
       Text(server) ++ Text(key.get("logMessage").toString) ++ <br/>
      })
      result
      //Text("")
  	}
  }

  /*
   lazy val date: Date = DependencyFactory.time.vend // create the date via factory

   def howdy = "#time *" #> date.toString
   */
}

