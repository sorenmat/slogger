name := "Slogger"

version := "1.0"

scalaVersion := "2.9.1"

 resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                    "releases"  at "http://oss.sonatype.org/content/repositories/releases")
                    
libraryDependencies += "org.mongodb" % "casbah_2.9.2" % "2.4.1"