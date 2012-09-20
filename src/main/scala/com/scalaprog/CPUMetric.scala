package com.scalaprog

import java.util.Date
import java.net.InetAddress

case class CPUMetric(val load: Double) {
  val key = Key(InetAddress.getLocalHost.getHostName, new java.util.Date().getTime, "" )
  //key.date = new Date().getTime

}