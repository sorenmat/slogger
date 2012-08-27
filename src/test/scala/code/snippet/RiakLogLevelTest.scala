package code.snippet

import com.basho.riak.client.RiakFactory
import com.scalaprog.{Info, Key, Msg}
import sjson.json.Serializer
import reflect.BeanInfo

@BeanInfo
case class Address(street: String, city: String, zip: String) {
  private def this() = this(null, null, null)

  override def toString = "address = " + street + "/" + city + "/" + zip
}

object RiakLogLevelTest {
  def main(args: Array[String]) {
    //val riakClient = RiakFactory.pbcClient(); //or RiakFactory.httpClient();
    val riakClient = RiakFactory.httpClient();

    // create a new bucket
    val myBucket = riakClient.createBucket("myBucket").execute();


    val serializer = Serializer.SJSON

    for(i <- 0 to 1000) {
      val key = Key("localhost", System.currentTimeMillis(), "test")
      val data = Msg(key, Info(), "Hello world "+i+" !!!")
      myBucket.store(key.toString, new String(serializer.out(Address("soren", "test", "4000")))).execute();

    }

    // add data to the bucket
    myBucket.store("key1", "value1").execute();

    //fetch it back
    var myData = myBucket.fetch("key1").execute();

    // you can specify extra parameters to the store operation using the
    // fluent builder style API
    myData = myBucket.store("key1", "value2").returnBody(true).execute();

    // delete
    myBucket.delete("key1").rw(3).execute();
  }
}
